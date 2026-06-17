package clients;

import com.fasterxml.jackson.core.type.TypeReference;
import filereader.ResourceProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.jsonplaceholder.JsonPlaceholderResponse;
import models.jsonplaceholder.Post;
import models.jsonplaceholder.User;
import utils.ApiUtils;

import java.util.List;

import static utils.ApiUtils.getFullEndpoint;

public class JsonPlaceholderClient {
    private static final String POSTS_ENDPOINT = "posts";
    private static final String USERS_ENDPOINT = "users";

    private static final ApiUtils API = new ApiUtils(ResourceProvider.getConfig().url);

    private JsonPlaceholderClient() {
    }

    public static Response getAllPosts() {
        return API.getRequest(getFullEndpoint(POSTS_ENDPOINT));
    }

    public static JsonPlaceholderResponse<List<Post>> getListPosts() {
        return JsonPlaceholderResponse.fromRawResponse(
                API.getRequest(getFullEndpoint(POSTS_ENDPOINT)), new TypeReference<List<Post>>() {
                });
    }

    public static Response getPostById(int id) {
        return API.getRequest(getFullEndpoint(POSTS_ENDPOINT, String.valueOf(id)));
    }

    public static Response createPost(Post post) {
        return API.sendRequest(getFullEndpoint(POSTS_ENDPOINT), ContentType.JSON, post);
    }

    public static JsonPlaceholderResponse<List<User>> getAllUsers() {
//        return API.getRequest(getFullEndpoint(USERS_ENDPOINT));
        return JsonPlaceholderResponse.fromRawResponse(
                API.getRequest(getFullEndpoint(USERS_ENDPOINT)), new TypeReference<List<User>>() {
                });
    }

    public static Response getUserById(int id) {
        return API.getRequest(getFullEndpoint(USERS_ENDPOINT, String.valueOf(id)));
    }
}
