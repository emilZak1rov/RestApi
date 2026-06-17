package clients;

import filereader.ResourceProvider;
import io.restassured.http.ContentType;
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

    public static JsonPlaceholderResponse<List<Post>> getAllPosts() {
        return JsonPlaceholderResponse.fromResponse(
                API.getRequest(getFullEndpoint(POSTS_ENDPOINT)),
                new com.google.common.reflect.TypeToken<List<Post>>() {
                }.getType());
    }

    public static JsonPlaceholderResponse<Post> getPostById(int id) {
        return JsonPlaceholderResponse.fromResponse(API.getRequest(getFullEndpoint(POSTS_ENDPOINT, String.valueOf(id))),
                Post.class);
    }

    public static JsonPlaceholderResponse<Post> createPost(Post post) {
        return JsonPlaceholderResponse.fromResponse(API.sendRequest(getFullEndpoint(POSTS_ENDPOINT), ContentType.JSON, post),
                Post.class);
    }

    public static JsonPlaceholderResponse<List<User>> getAllUsers() {
        return JsonPlaceholderResponse.fromResponse(
                API.getRequest(getFullEndpoint(USERS_ENDPOINT)),
                new com.google.common.reflect.TypeToken<List<User>>() {
                }.getType());
    }

    public static JsonPlaceholderResponse<User> getUserById(int id) {
        return JsonPlaceholderResponse.fromResponse(API.getRequest(getFullEndpoint(USERS_ENDPOINT, String.valueOf(id))),
                User.class);
    }
}
