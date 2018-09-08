package com.poseidon.fridge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class WebFoodControllerTests {
    
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
    
    @Test
    public void registerFood() {
        String name = "파스퇴르 우유 1.8L";
        int quantity = 1;
        
        browser.get(BASE_URL + "/web/foods/add");
        WebElement nameElement = browser.findElement(By.name("name"));
        WebElement quantityElement = browser.findElement(By.name("quantity"));
        WebElement expiryDateElement = browser.findElement(By.name("expiryDate"));
        nameElement.sendKeys(name);
        quantityElement.sendKeys(Integer.toString(quantity));
        expiryDateElement.sendKeys("2016");
        expiryDateElement.sendKeys(Keys.TAB);
        expiryDateElement.sendKeys("0908");
        browser.findElementByTagName("form").submit();
        
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("식품을 저장했습니다.");
    }
    
}
