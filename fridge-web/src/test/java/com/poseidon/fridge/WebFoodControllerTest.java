package com.poseidon.fridge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class WebFoodControllerTest {
    
    private static ChromeDriver browser;
    
    @Value("${local.server.port}")
    private int port;
    
    private String basePath = "http://localhost";
    private static String BASE_URL;

    @BeforeClass
    public static void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "/Users/gang-yeongho/Documents/chromedriver");
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }
    
    @Before
    public void setUp() {
        BASE_URL = basePath + ":" + port;
    }
    
    @Test
    public void helloWorld() {
        browser.get(BASE_URL + "/web/foods");
        assertThat(browser.findElementByTagName("h2").getText()).isEqualTo("Hello, world!");
    }
    
}
