package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static boolean isJson(String body) {
        try {
            objectMapper.readTree(body);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
