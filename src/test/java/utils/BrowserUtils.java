package utils;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import filereader.ResourceProvider;

public class BrowserUtils {
    private static Browser browser;

    private BrowserUtils() {
    }

    public static void setBrowser() {
        browser = AqualityServices.getBrowser();
        if (ResourceProvider.getConfig().windowSize.equals("maximized")) {
            browser.maximize();
        }
    }

    public static void openSite(String url) {
        browser.goTo(url);
    }

    public static void waitForPageToLoad() {
        browser.waitForPageToLoad();
    }

    public static void quit() {
        browser.quit();
    }
}
