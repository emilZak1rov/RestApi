package tests;

import filereader.ResourceProvider;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = ResourceProvider.getConfig().url;
    }
}
