package tests.restapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import models.jsonplaceholder.User;

@NoArgsConstructor
@Data
public class UserTestData {
    public int expectedStatusCode;
    public User user;
}
