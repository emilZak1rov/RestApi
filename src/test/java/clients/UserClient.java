package clients;

import io.restassured.response.Response;
import utils.ApiUtils;

public class UserClient {
    private static final String USERS_ENDPOINT = "/users";
    private static final String USERS_ENDPOINT_WITH_ID = "/users/%d";

    private UserClient() {
    }

    public static Response getAllUsers() {
        return ApiUtils.getRequest(USERS_ENDPOINT);
    }

    public static Response getUserById(int id) {
        return ApiUtils.getRequest(String.format(USERS_ENDPOINT_WITH_ID, id));
    }
}
