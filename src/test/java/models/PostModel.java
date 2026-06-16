package models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PostModel {
    public int expectedStatusCode;
    public int userId;
    public int id;
    public String title;
    public String body;
}
