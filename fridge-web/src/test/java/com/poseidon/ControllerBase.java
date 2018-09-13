package com.poseidon;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class ControllerBase {
    
    protected static ChromeDriver browser;
    
    @Value("${local.server.port}")
    private int port;
    private String host = "http://localhost";
    protected static String BASE_URL;
    
    @Before
    public void init() {
        BASE_URL = host + ":" + port;
        setUp();
    }
    
    protected abstract void setUp();
    
    @BeforeClass
    public static void openBrowser() {
        String home = System.getProperty("user.home");
        System.setProperty("webdriver.chrome.driver", home + "/Documents/chromedriver");
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

}
