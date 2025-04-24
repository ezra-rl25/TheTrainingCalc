package APIImplementation;

import javastrava.api.API;
import javastrava.api.AuthorisationAPI;
import javastrava.auth.model.Token;
import javastrava.auth.model.TokenResponse;
import javastrava.model.StravaActivity;
import javastrava.model.StravaAthlete;
import javastrava.service.Strava;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class Authorization {

    private final StravaAthlete athlete;

    public Authorization() {
        int clientId = 154726;
        String clientSecret = "5ca1c6020e03e0561c6bacfda503340bd42bd9ef";
        String redirectUri = "http://localhost:8080/exchange_token";

        // ✅ Prevent the JCS /cache.ccf crash
        System.setProperty("javastrava.disable.cache", "true");

        AuthorisationAPI auth = API.authorisationInstance();
        TokenResponse response = TokenStore.loadToken();

        try {
            if (response == null) {
                String code = fetchOAuthCodeViaBrowser(clientId, redirectUri);
                response = auth.tokenExchange(clientId, clientSecret, code);
                TokenStore.saveToken(response);
                System.out.println("✅ New token obtained and saved.");
            } else if (isExpired(response)) {
                response = auth.refreshToken(clientId, clientSecret, "refresh_token", response.getRefreshToken());
                TokenStore.saveToken(response);
                System.out.println("🔁 Token was refreshed!");
                System.out.println("New token expires at: " + response.getExpiresAt());
            }
        } catch (Exception e) {
            throw new RuntimeException("Authorization failed", e);
        }

        Token token = new Token(response);
        Strava strava = new Strava(token);
        this.athlete = strava.getAuthenticatedAthlete();
    }

    public String getCity() {
        return athlete.getCity();
    }

    public String getName() {
        return athlete.getFirstname() + " " + athlete.getLastname();
    }


    private boolean isExpired(TokenResponse token) {
        long now = System.currentTimeMillis() / 1000L;
        return token.getExpiresAt() < now;
    }

    private String fetchOAuthCodeViaBrowser(int clientId, String redirectUri) throws IOException {
        String authUrl = "https://www.strava.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&approval_prompt=force" +
                "&scope=read,activity:read";

        Desktop.getDesktop().browse(URI.create(authUrl));
        System.out.println("🔁 Waiting for OAuth redirect on " + redirectUri);
        return OAuthServer.waitForCode();
    }
}