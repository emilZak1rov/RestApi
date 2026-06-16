package utils;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomUtils {
    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private RandomUtils() {
    }

    public static String randomString(int length) {
        return randomString(length, ALPHABET);
    }

    public static String randomString(int length, String characters) {
        if (length <= 0) {
            return "";
        }
        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Набор символов не может быть пустым");
        }

        return IntStream.range(0, length)
                .map(i -> characters.charAt(RANDOM.nextInt(characters.length())))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }
}
