package clients;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Post;
import utils.ApiUtils;

public class PostsClient {
    private static final String POSTS_ENDPOINT = "/posts";
    private static final String POSTS_ENDPOINT_WITH_ID = "/posts/%d";

    private PostsClient() {
    }

    public static Response getAllPosts() {
        return ApiUtils.getRequest(POSTS_ENDPOINT);
    }

    public static Response getPostById(int id) {
        return ApiUtils.getRequest(String.format(POSTS_ENDPOINT_WITH_ID, id));
    }

    public static Response createPost(Post post) {
        return ApiUtils.sendRequest(POSTS_ENDPOINT, ContentType.JSON, post);
    }
}
