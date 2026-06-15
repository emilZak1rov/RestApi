package filereader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import utils.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDataReader {
    private static final Gson GSON = new GsonBuilder().create();

    private FileDataReader() {
    }

    public static String readFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            LogUtils.logError("Путь к файлу не может быть null или пустым");
            throw new IllegalArgumentException("Путь к файлу не может быть null или пустым");
        }
        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            if (content.trim().isEmpty()) {
                LogUtils.logError(String.format("Файл пуст: %s", filePath));
                throw new IllegalStateException(String.format("Файл пуст: %s", filePath));
            }
            return content;
        } catch (IOException e) {
            LogUtils.logError(String.format("Ошибка при чтении файла: %s", filePath));
            throw new RuntimeException(String.format("Ошибка при чтении файла: %s", filePath), e);
        }
    }

    public static <T> T readAndParse(String filePath, Class<T> classType) {
        String jsonContent = readFile(filePath);
        try {
            return GSON.fromJson(jsonContent, classType);
        } catch (JsonSyntaxException e) {
            LogUtils.logError(String.format("Не удалось распарсить JSON в объект типа %s", classType.getSimpleName()));
            throw new RuntimeException(String.format("Не удалось распарсить JSON в объект типа %s", classType.getSimpleName()), e);
        }
    }
}
