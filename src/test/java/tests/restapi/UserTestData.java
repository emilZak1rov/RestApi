package tests.restapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import models.Address;
import models.Company;
import models.User;

@NoArgsConstructor
@Data
public class UserTestData {
    public int expectedStatusCode;
    public int id;
    public String name;
    public String username;
    public String email;
    public Address address;
    public String phone;
    public String website;
    public Company company;

    public static User getUser(UserTestData user) {
        return new User(user.id, user.name, user.username, user.email, user.address, user.phone, user.website, user.company);
    }

    public User getUser() {
        return getUser(this);
    }
}
