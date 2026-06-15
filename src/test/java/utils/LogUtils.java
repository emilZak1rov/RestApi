package utils;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.core.logging.Logger;

public class LogUtils {
    private static final Logger LOGGER = AqualityServices.getLogger();

    private LogUtils() {
    }

    public static void logInfo(String str) {
        LOGGER.info(String.format("[INFO] %s", str));
    }

    public static void logError(String str) {
        LOGGER.error(String.format("[ERROR] %s", str));
    }
}
