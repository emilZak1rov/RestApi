package tests.restapi;

import clients.JsonPlaceHolderClient;
import filereader.FileDataReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.LogUtils;

import java.net.HttpURLConnection;
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
        Response response = JsonPlaceHolderClient.getRequest("/posts");

        LogUtils.logInfo("Проверка кода состояния");
        Assert.assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
                String.format("Код состояния должен быть %s, но получен: %s", HttpURLConnection.HTTP_OK, response.getStatusCode()));

        String contentType = response.getContentType();
        Assert.assertTrue(contentType != null && contentType.contains("application/json"),
                String.format("Content-Type должен быть application/json, но получен: %s", contentType));

        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("id");

        Assert.assertNotNull(ids, "Список сообщений не должен быть null");
        Assert.assertTrue(ids.size() > 0, "Список сообщений не должен быть пустым");
        Assert.assertTrue(isSortedAscending(ids),
                "Сообщения не отсортированы по id в порядке возрастания");

        LogUtils.logInfo(" 2. Отправьте запрос GET, чтобы получить пост с id=99 (/posts/99).");
        response = JsonPlaceHolderClient.getRequest("/posts/99");

        LogUtils.logInfo("Проверка кода состояния");
        Assert.assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
                String.format("Код состояния должен быть %s, но получен: %s", HttpURLConnection.HTTP_OK, response.getStatusCode()));

        LogUtils.logInfo("Проверка информации о сообщении");
        int expectedUserId = data.userId;
        int expectedId = data.id;
        int userId = response.jsonPath().getInt("userId");
        int id = response.jsonPath().getInt("id");

        Assert.assertEquals(userId, expectedUserId, String.format("Ожидаемое userId=%s, фактическое userId=%s", expectedUserId, userId));
        Assert.assertEquals(id, expectedId, String.format("Ожидаемое id=%s, фактическое id=%s", expectedId, id));
        Assert.assertNotNull(response.jsonPath().getString("title"), String.format("title пустой у id=%s", id));
        Assert.assertNotNull(response.jsonPath().getString("body"), String.format("body пустое у id=%s", id));

        LogUtils.logInfo("3. Отправьте запрос GET, чтобы получить пост с id=150 (/posts/150).");
        response = JsonPlaceHolderClient.getRequest("/posts/150");

        LogUtils.logInfo("Проверка кода состояния");
        Assert.assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_NOT_FOUND,
                String.format("Код состояния должен быть %s, но получен: %s", HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCode()));

        LogUtils.logInfo("Проверка body");
        Assert.assertNull(response.jsonPath().getString("body"));

        LogUtils.logInfo("4. Отправьте POST-запрос, чтобы создать сообщение с userId=1 и случайным телом и случайным заголовком (/posts).");

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
