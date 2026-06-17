package utils;

import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.InvalidArgumentException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ApiUtils {
    private final String baseUri;
    private final RestAssuredConfig config;

    public ApiUtils(String baseUri, RestAssuredConfig config) {
        if (baseUri == null || baseUri.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URI cannot be null or empty");
        }
        this.baseUri = baseUri;
        this.config = config;
    }

    public ApiUtils(String baseUri) {
        this(baseUri, null);
    }

    public Response getRequest(String endpoint) {
        RequestSpecification request = given();
        if (config != null) {
            request = request.config(config);
        }
        return request
                .baseUri(baseUri)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response sendRequest(String endpoint, ContentType contentType, Object requestBody) {
        RequestSpecification request = given();
        if (config != null) {
            request = request.config(config);
        }
        return request
                .baseUri(baseUri)
                .contentType(contentType)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static String getFullEndpoint(String... segments) {
        if (segments == null || segments.length == 0) {
            throw new InvalidArgumentException("Необходимо передать не пустые аргументы");
        }

        String fullPath = Arrays.stream(segments)
                .map(segment -> {
                    if (segment == null) return "";
                    return segment.replaceAll("^[/]+|[/]+$", "");
                })
                .filter(segment -> !segment.isEmpty())
                .collect(Collectors.joining("/"));
        return "/" + fullPath;
    }
}
