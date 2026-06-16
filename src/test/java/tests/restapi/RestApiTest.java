package tests.restapi;

import clients.PostsClient;
import filereader.FileDataReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.PostModel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.LogUtils;
import utils.RandomUtils;

import java.util.List;

public class RestApiTest extends BaseTest {
    @DataProvider(name = "RestApiModel")
    public Object[][] getTimerData() {
        RestApiModel dataModel = FileDataReader.readAndParse("src/test/java/tests/restapi/restapi.json", RestApiModel.class);
        return new Object[][]{
                {dataModel}
        };
    }

    @Test(dataProvider = "RestApiModel")
    public void Test(RestApiModel data) {
        LogUtils.logInfo("1. Отправьте запрос GET, чтобы получить все сообщения (/posts).");
        Response response = PostsClient.getAllPosts();

        assertStatusCode(response.getStatusCode(), data.step1.expectedStatusCode);

        LogUtils.logInfo("Проверка формата файла");
        String contentType = response.getContentType();
        Assert.assertTrue(contentType != null && contentType.contains("application/json"),
                String.format("Content-Type должен быть application/json, но получен: %s", contentType));

        LogUtils.logInfo("Проверка сортировки сообщений по возрастанию");
        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("id");

        Assert.assertNotNull(ids, "Список сообщений не должен быть null");
        Assert.assertTrue(ids.size() > 0, "Список сообщений не должен быть пустым");
        Assert.assertTrue(isSortedAscending(ids),
                "Сообщения не отсортированы по id в порядке возрастания");

        LogUtils.logInfo(String.format("2. Отправьте запрос GET, чтобы получить пост с id=%d (/posts/%d).", data.step2.id, data.step2.id));
        response = PostsClient.getPostById(data.step2.id);

        assertStatusCode(response.getStatusCode(), data.step2.expectedStatusCode);

        LogUtils.logInfo("Проверка информации о сообщении");
        int expectedUserId = data.step2.userId;
        int expectedId = data.step2.id;
        int userId = response.jsonPath().getInt("userId");
        int id = response.jsonPath().getInt("id");

        Assert.assertEquals(userId, expectedUserId, String.format("Ожидаемое userId=%s, фактическое userId=%s", expectedUserId, userId));
        Assert.assertEquals(id, expectedId, String.format("Ожидаемое id=%s, фактическое id=%s", expectedId, id));
        Assert.assertNotNull(response.jsonPath().getString("title"), String.format("title пустой у id=%s", id));
        Assert.assertNotNull(response.jsonPath().getString("body"), String.format("body пустое у id=%s", id));

        LogUtils.logInfo(String.format("3. Отправьте запрос GET, чтобы получить пост с id=%d (/posts/%d).",
                data.step3.id, data.step3.id));
        response = PostsClient.getPostById(data.step3.id);

        assertStatusCode(response.getStatusCode(), data.step3.expectedStatusCode);

        LogUtils.logInfo("Проверка body");
        Assert.assertEquals(response.jsonPath().getString("body"), data.step3.body,
                String.format("body не пустое, = %s", response.jsonPath().getString("body")));

        LogUtils.logInfo(String.format(
                "4. Отправьте POST-запрос, чтобы создать сообщение с userId=%d и случайным телом и случайным заголовком (/posts).",
                data.step4.userId));
        PostModel post = new PostModel();
        post.userId = data.step4.userId;
        post.body = RandomUtils.randomString(5);
        post.title = RandomUtils.randomString(6);

        response = PostsClient.createPost(post);

        assertStatusCode(response.getStatusCode(), data.step4.expectedStatusCode);

        LogUtils.logInfo("Проверка отправленного сообщения");
        PostModel createdPost = response.getBody().as(PostModel.class);
        Assert.assertEquals(post.userId, createdPost.userId,
                String.format("userId не совпал, send=%d, created=%d", post.userId, createdPost.userId));
        Assert.assertEquals(post.title, createdPost.title,
                String.format("title не совпал, send=%s, created=%s", post.title, createdPost.title));
        Assert.assertEquals(post.body, createdPost.body,
                String.format("body не совпал, send=%s, created=%s", post.body, createdPost.body));
        Assert.assertTrue(createdPost.id > 0, "id не создался");


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
        Assert.assertEquals(actual, expected,
                String.format("Код состояния должен быть %d, но получен: %d", expected, actual));
    }
}
