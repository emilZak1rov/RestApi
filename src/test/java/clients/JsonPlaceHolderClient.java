package clients;

import io.restassured.response.Response;
import utils.ApiUtils;

public class JsonPlaceHolderClient {
    private JsonPlaceHolderClient() {
    }

    public static Response getRequest(String endpoint) {
        return ApiUtils.getRequest(endpoint);
    }
}
