package com.poseidon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class ControllerBase {
    
    protected static ChromeDriver browser;
    
    private @LocalServerPort int port;
    private String host = "http://localhost";
    protected static String BASE_URL;
    
    @Autowired
    protected RestTemplate restTemplate;
    
    protected static final String CORE_API_URL = "http://fridge-service";
    
    @Before
    public void init() {
        BASE_URL = host + ":" + port;
        restTemplate.delete(CORE_API_URL + "/fridges", Collections.emptyMap());
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
        
        ResponseEntity<FridgeCommand> response = restTemplate.postForEntity(CORE_API_URL + "/fridges", fridgeCommand, FridgeCommand.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        FridgeCommand fridge = response.getBody();
        assertThat(fridge.getId()).isPositive();
        return fridge;
    }
    
    protected FoodCommand createFood(FoodCommand foodCommand) {
        ResponseEntity<FoodCommand> response = restTemplate.postForEntity(CORE_API_URL + "/foods", foodCommand, FoodCommand.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        FoodCommand food = response.getBody();
        assertThat(food.getId()).isPositive();
        return food;
    }

}
