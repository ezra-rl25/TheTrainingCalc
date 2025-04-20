package APIImplementation;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class OAuthServer {
    private static String code;

    public static String waitForCode() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/exchange_token", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("code=")) {
                code = query.split("code=")[1].split("&")[0];
                String response = "✅ Authorization successful! You can close this tab.";
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                server.stop(0);
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("🔁 Waiting for redirect on http://localhost:8080/exchange_token ...");

        while (code == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        return code;
    }
}