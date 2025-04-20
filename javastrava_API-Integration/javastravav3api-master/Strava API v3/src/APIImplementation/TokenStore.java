package APIImplementation;

import com.google.gson.*;
import javastrava.auth.model.TokenResponse;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class TokenStore {
    private static final String FILE_PATH = "token.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    public static void saveToken(TokenResponse token) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(token, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TokenResponse loadToken() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, TokenResponse.class);
        } catch (IOException e) {
            return null;
        }
    }

    // Handles ZonedDateTime serialization/deserialization
    private static class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ZonedDateTime.parse(json.getAsString());
        }
    }
}
