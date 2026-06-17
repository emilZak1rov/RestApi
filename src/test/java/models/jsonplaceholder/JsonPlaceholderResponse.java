package models.jsonplaceholder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.net.HttpURLConnection;

public record JsonPlaceholderResponse<T>(int statusCode, String contentType, T body) {
    public static <T> JsonPlaceholderResponse<T> fromRawResponse(Response response, Class<T> responseType) {
        int statusCode = response.statusCode();
        String contentType = response.getHeader("Content-Type");
        ObjectMapper objectMapper = new ObjectMapper();

        if (statusCode >= HttpURLConnection.HTTP_OK && statusCode < HttpURLConnection.HTTP_MULT_CHOICE) {
            try {
                String bodyString = response.getBody().toString();
                T body = objectMapper.readValue(bodyString, responseType);
                return new JsonPlaceholderResponse<>(statusCode, contentType, body);
            } catch (Exception e) {
                return new JsonPlaceholderResponse<>(statusCode, contentType, null);
            }
        } else {
            return new JsonPlaceholderResponse<>(statusCode, contentType, null);
        }
    }

    public static <T> JsonPlaceholderResponse<T> fromRawResponse(Response response, TypeReference<T> responseType) {
        int statusCode = response.statusCode();
        String contentType = response.getHeader("Content-Type");
        ObjectMapper objectMapper = new ObjectMapper();

        if (statusCode >= HttpURLConnection.HTTP_OK && statusCode < HttpURLConnection.HTTP_MULT_CHOICE) {
            try {
                String bodyString = response.getBody().asString();
                T body = objectMapper.readValue(bodyString, responseType);
                return new JsonPlaceholderResponse<>(statusCode, contentType, body);
            } catch (Exception e) {
                return new JsonPlaceholderResponse<>(statusCode, contentType, null);
            }
        } else {
            return new JsonPlaceholderResponse<>(statusCode, contentType, null);
        }
    }
}
