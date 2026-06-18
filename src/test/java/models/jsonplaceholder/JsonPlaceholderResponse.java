package models.jsonplaceholder;

import io.restassured.response.Response;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public class JsonPlaceholderResponse<T> {
    private final int statusCode;
    private final String contentType;
    private final T body;

    private JsonPlaceholderResponse(int statusCode, String contentType, T body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    public static <T> JsonPlaceholderResponse<T> fromResponse(Response response, Type type) {
        int statusCode = response.getStatusCode();
        String contentType = response.getContentType();
        String bodyAsString = response.getBody().asString();
        T deserializedBody = null;

        if (bodyAsString != null && !bodyAsString.isEmpty() && type != null) {
            deserializedBody = response.getBody().as(type);
        }
        return new JsonPlaceholderResponse<>(statusCode, contentType, deserializedBody);
    }

    public boolean isBodyDeserialized() {
        return body != null;
    }
}