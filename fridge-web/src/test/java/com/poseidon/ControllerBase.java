package com.poseidon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;
import com.poseidon.fridge.service.FridgeClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class ControllerBase {
    
    protected static ChromeDriver browser;
    
    private @LocalServerPort int port;
    private String host = "http://localhost";
    protected static String BASE_URL;
    
    @Autowired
    private FridgeClient fridgeClient;
    
    @Before
    public void init() {
        BASE_URL = host + ":" + port;
        fridgeClient.deleteAll();
        
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
    
    protected FridgeCommand createFridge(String nickname, long userId) {
        FridgeCommand fridgeCommand = FridgeCommand.builder()
                .nickname(nickname)
                .userId(userId)
                .build();
        
        FridgeCommand fridge = fridgeClient.generate(fridgeCommand);
        assertThat(fridge.getId()).isPositive();
        return fridge;
    }
    
    protected FoodCommand createFood(FoodCommand foodCommand) {
        FoodCommand food = fridgeClient.createFood(foodCommand);
        assertThat(food.getId()).isPositive();
        return food;
    }

}
