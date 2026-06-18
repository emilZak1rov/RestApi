package tests.restapi;

import clients.JsonPlaceholderClient;
import filereader.FileDataReader;
import models.jsonplaceholder.JsonPlaceholderResponse;
import models.jsonplaceholder.Post;
import models.jsonplaceholder.User;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;
import utils.LogUtils;
import utils.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        SoftAssert softAssert = new SoftAssert();
        LogUtils.logInfo("1. Отправьте запрос GET, чтобы получить все сообщения (/posts).");
        JsonPlaceholderResponse<List<Post>> allPosts = JsonPlaceholderClient.getAllPosts();

        LogUtils.logInfo("Проверка кода состояния и формата файла");
        softAssert.assertEquals(allPosts.getStatusCode(), data.step1.expectedStatusCode, "Код состояния не совпадает");
        softAssert.assertTrue(allPosts.isFormatJson(), "Формат файла не json");
        softAssert.assertAll();

        LogUtils.logInfo("Проверка сортировки сообщений по возрастанию");
        List<Post> ids = allPosts.getBody();

        Assert.assertNotNull(ids, "Список сообщений не должен быть null");
        Assert.assertFalse(ids.isEmpty(), "Список сообщений не должен быть пустым");
        Assert.assertTrue(isSortedAscending(ids.stream().map(post -> post.id).collect(Collectors.toList())),
                "Сообщения не отсортированы по id в порядке возрастания");

        LogUtils.logInfo(String.format("2. Отправьте запрос GET, чтобы получить пост с id=%d (/posts/%d).",
                data.step2.post.id, data.step2.post.id));
        JsonPlaceholderResponse<Post> postResponse = JsonPlaceholderClient.getPostById(data.step2.post.id);

        Assert.assertEquals(postResponse.getStatusCode(), data.step2.expectedStatusCode, "Код состояния не совпадает");

        LogUtils.logInfo("Проверка информации о сообщении");
        int expectedUserId = data.step2.post.userId;
        int expectedId = data.step2.post.id;
        int userId = postResponse.getBody().userId;
        int id = postResponse.getBody().id;

        Assert.assertEquals(userId, expectedUserId, "userId не совпадает");
        Assert.assertEquals(id, expectedId, "id не совпадает");
        Assert.assertNotNull(postResponse.getBody().title, "title пустой");
        Assert.assertNotNull(postResponse.getBody().body, "body пустой");

        LogUtils.logInfo(String.format("3. Отправьте запрос GET, чтобы получить пост с id=%d (/posts/%d).",
                data.step3.post.id, data.step3.post.id));
        postResponse = JsonPlaceholderClient.getPostById(data.step3.post.id);

        Assert.assertEquals(postResponse.getStatusCode(), data.step3.expectedStatusCode, "Код состояния не совпадает");

        LogUtils.logInfo("Проверка body");
        Assert.assertEquals(postResponse.getBody().body, data.step3.post.body, "body не пустое");

        LogUtils.logInfo(String.format(
                "4. Отправьте POST-запрос, чтобы создать сообщение с userId=%d и случайным телом и случайным заголовком (/posts).",
                data.step4.post.userId));
        Post sendPost = new Post();
        sendPost.userId = data.step4.post.userId;
        sendPost.body = RandomUtils.randomString(5);
        sendPost.title = RandomUtils.randomString(6);

        postResponse = JsonPlaceholderClient.createPost(sendPost);

        Assert.assertEquals(postResponse.getStatusCode(), data.step4.expectedStatusCode, "Код состояния не совпадает");

        LogUtils.logInfo("Проверка отправленного сообщения");
        Post createdPost = postResponse.getBody();
        Assert.assertEquals(sendPost.userId, createdPost.userId,
                "userId не совпадает");
        Assert.assertEquals(sendPost.title, createdPost.title,
                "title не совпадает");
        Assert.assertEquals(sendPost.body, createdPost.body,
                "body не совпадает");
        Assert.assertTrue(createdPost.id > 0, "id не создался");

        LogUtils.logInfo("5. Отправьте запрос GET, чтобы получить пользователей (/users).");
        JsonPlaceholderResponse<List<User>> allUsersResponse = JsonPlaceholderClient.getAllUsers();

        LogUtils.logInfo("Проверка кода состояния и формата файла");
        softAssert.assertEquals(allUsersResponse.getStatusCode(), data.step5.expectedStatusCode, "Код состояния не совпадает");
        softAssert.assertTrue(allUsersResponse.isFormatJson(), "Формат файла не json");
        softAssert.assertAll();

        LogUtils.logInfo("Проверка совпадения ожидаемых и фактических пользовательских данных");
        List<User> users = allUsersResponse.getBody();

        Assert.assertNotNull(users, "Пользователи null");
        Assert.assertFalse(users.isEmpty(), "Пользователей 0");

        User actualUser = users.stream().filter(user -> user.getId() == data.step5.user.id).findFirst().orElse(null);
        Assert.assertEquals(actualUser, data.step5.getUser(), "Пользовательские данные не равны");

        LogUtils.logInfo(String.format("6. Отправьте запрос GET, чтобы получить пользователя с id=%d (/users/%d).",
                data.step6.expectedStatusCode, data.step6.user.id));
        JsonPlaceholderResponse<User> userResponse = JsonPlaceholderClient.getUserById(data.step6.user.id);

        Assert.assertEquals(userResponse.getStatusCode(), data.step6.expectedStatusCode, "Код состояния не совпадает");

        LogUtils.logInfo("Проверка совпадения пользовательских данных с данными на предыдущем шаге");
        Assert.assertEquals(userResponse.getBody(), actualUser);
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
}
