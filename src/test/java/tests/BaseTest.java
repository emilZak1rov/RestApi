package tests;

import filereader.ResourceProvider;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.BrowserUtils;

public abstract class BaseTest {
    @BeforeMethod
    public void setUp() {
        BrowserUtils.setBrowser();
        BrowserUtils.openSite(ResourceProvider.getConfig().url);
        BrowserUtils.waitForPageToLoad();
    }

    @AfterMethod
    public void tearDown() {
        BrowserUtils.quit();
    }
}
