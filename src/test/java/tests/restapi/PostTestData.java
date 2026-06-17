package tests.restapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import models.jsonplaceholder.Post;

@NoArgsConstructor
@Data
public class PostTestData {
    public int expectedStatusCode;
    public Post post;
}
