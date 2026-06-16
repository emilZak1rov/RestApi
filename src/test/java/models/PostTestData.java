package models;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostTestData {
    public int expectedStatusCode;
    public int userId;
    public int id;
    public String title;
    public String body;

    public static Post getPost(PostTestData data) {
        return new Post(data.userId, data.id, data.title, data.body);
    }

    public Post getPost() {
        return new Post(userId, id, title, body);
    }
}
