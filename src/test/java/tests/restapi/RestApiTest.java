package tests.restapi;

import clients.PostsClient;
import clients.UserClient;
import filereader.FileDataReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.jsonplaceholder.Post;
import models.jsonplaceholder.User;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.LogUtils;
import utils.RandomUtils;

import java.util.Arrays;
import java.util.List;

public class RestApiTest extends BaseTest {
    @DataProvider(name = "RestApiModel")
    public Object[][] getTimerData() {
        RestApiTestData dataModel = FileDataReader.readAndParse("src/test/java/tests/restapi/restapi.json", RestApiTestData.class);
        return new Object[][]{
                {dataModel}
        };
    }

    @Test(dataProvider = "RestApiModel")
    public void Test(RestApiTestData data) {
        LogUtils.logInfo("1. Отправьте запрос GET, чтобы получить все сообщения (/posts).");
        Response response = PostsClient.getAllPosts();

        assertStatusCode(response.getStatusCode(), data.step1.expectedStatusCode);
        assertFileFormat(response.getContentType(), "application/json");

        LogUtils.logInfo("Проверка сортировки сообщений по возрастанию");
        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("id");

        Assert.assertNotNull(ids, "Список сообщений не должен быть null");
        Assert.assertFalse(ids.isEmpty(), "Список сообщений не должен быть пустым");
        Assert.assertTrue(isSortedAscending(ids),
                "Сообщения не отсортированы по id в порядке возрастания");

        LogUtils.logInfo(String.format("2. Отправьте запрос GET, чтобы получить пост с id=%d (/posts/%d).",
                data.step2.post.id, data.step2.post.id));
        response = PostsClient.getPostById(data.step2.post.id);

        assertStatusCode(response.getStatusCode(), data.step2.expectedStatusCode);

        LogUtils.logInfo("Проверка информации о сообщении");
        int expectedUserId = data.step2.post.userId;
        int expectedId = data.step2.post.id;
        int userId = response.jsonPath().getInt("userId");
        int id = response.jsonPath().getInt("id");

        Assert.assertEquals(userId, expectedUserId, "userId не совпадает");
        Assert.assertEquals(id, expectedId, "id не совпадает");
        Assert.assertNotNull(response.jsonPath().getString("title"), "title пустой");
        Assert.assertNotNull(response.jsonPath().getString("body"), "body пустой");

        LogUtils.logInfo(String.format("3. Отправьте запрос GET, чтобы получить пост с id=%d (/posts/%d).",
                data.step3.post.id, data.step3.post.id));
        response = PostsClient.getPostById(data.step3.post.id);

        assertStatusCode(response.getStatusCode(), data.step3.expectedStatusCode);

        LogUtils.logInfo("Проверка body");
        Assert.assertEquals(response.jsonPath().getString("body"), data.step3.post.body,
                "body не пустое");

        LogUtils.logInfo(String.format(
                "4. Отправьте POST-запрос, чтобы создать сообщение с userId=%d и случайным телом и случайным заголовком (/posts).",
                data.step4.post.userId));
        Post post = new Post();
        post.userId = data.step4.post.userId;
        post.body = RandomUtils.randomString(5);
        post.title = RandomUtils.randomString(6);

        response = PostsClient.createPost(post);

        assertStatusCode(response.getStatusCode(), data.step4.expectedStatusCode);

        LogUtils.logInfo("Проверка отправленного сообщения");
        Post createdPost = response.getBody().as(Post.class);
        Assert.assertEquals(post.userId, createdPost.userId,
                "userId не совпадает");
        Assert.assertEquals(post.title, createdPost.title,
                "title не совпадает");
        Assert.assertEquals(post.body, createdPost.body,
                "body не совпадает");
        Assert.assertTrue(createdPost.id > 0, "id не создался");

        LogUtils.logInfo("5. Отправьте запрос GET, чтобы получить пользователей (/users).");
        response = UserClient.getAllUsers();

        assertStatusCode(response.getStatusCode(), data.step5.expectedStatusCode);
        assertFileFormat(response.getContentType(), "application/json");

        LogUtils.logInfo("Проверка совпадения ожидаемых и фактических пользовательских данных");
        List<User> users = Arrays.asList(response.getBody().as(User[].class));

        Assert.assertNotNull(users, "Пользователи null");
        Assert.assertFalse(users.isEmpty(), "Пользователей 0");

        User actualUser = users.stream().filter(user -> user.getId() == data.step5.user.id).findFirst().orElse(null);
        Assert.assertEquals(actualUser, data.step5.getUser(), "Пользовательские данные не равны");

        LogUtils.logInfo(String.format("6. Отправьте запрос GET, чтобы получить пользователя с id=%d (/users/%d).",
                data.step6.expectedStatusCode, data.step6.user.id));
        response = UserClient.getUserById(data.step6.user.id);

        assertStatusCode(response.getStatusCode(), data.step6.expectedStatusCode);

        LogUtils.logInfo("Проверка совпадения пользовательских данных с данными на предыдущем шаге");
        Assert.assertEquals(response.getBody().as(User.class), actualUser);
    }

    private boolean isSortedAscending(List<Integer> list) {
        if (list == null || list.size() <= 1) {
            return true;
        }

        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void assertStatusCode(int actual, int expected) {
        LogUtils.logInfo("Проверка кода состояния");
        Assert.assertEquals(actual, expected, "Код состояния не равен ожидаемому");
    }

    private void assertFileFormat(String contentType, String expectedContentType) {
        LogUtils.logInfo("Проверка формата файла");
        Assert.assertTrue(contentType != null && contentType.contains(expectedContentType),
                "Content-type не равен ожидаемому");
    }
}
