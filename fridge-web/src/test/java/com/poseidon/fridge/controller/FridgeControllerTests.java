package com.poseidon.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.poseidon.ControllerBase;
import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

public class FridgeControllerTests extends ControllerBase {
    private FridgeCommand fridge;
    private static final Long USER_ID = 1004L;
    private FoodCommand food;
    
    @Override
    protected void setUp() {
        fridge = createFridge("나의 냉장고", USER_ID);
        food = new FoodCommand();
        food.setName("파스퇴르 우유 1.8L");
        food.setQuantity(1);
        food.setExpiryDate(LocalDate.now());
    }
    
    @Test
    public void hasContentsFromFridgesTable() {
        food.setFridge(fridge);
        FoodCommand foodCommand = createFood(food);
        
        browser.get(BASE_URL + "/fridges/me");
        WebElement table = browser.findElement(By.tagName("table"));
        assertThat(table.isDisplayed()).isTrue();
        
        List<WebElement> tr = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        assertThat(tr.size()).isEqualTo(1);
        
        List<WebElement> td = tr.get(0).findElements(By.tagName("td"));
        assertThat(td.get(0).getText()).isEqualTo("1");
        assertThat(td.get(1).findElement(By.tagName("a")).getText()).isEqualTo(foodCommand.getName());
        assertThat(td.get(2).getText()).isEqualTo(Integer.toString(foodCommand.getQuantity()));
        assertThat(td.get(3).getText()).isEqualTo(foodCommand.getExpiryDate().toString());
        assertThat(td.get(4).findElement(By.tagName("a")).getAttribute("href")).containsPattern(Pattern.compile(BASE_URL + "/fridges/foods/delete/[0-9]"));
    }
    
    @Test
    public void findRegistrationFoodButtonAndClick() {
        browser.get(BASE_URL + "/fridges/me");
        browser.findElement(By.id("btnRegistrationFood")).click();
        assertThat(browser.getCurrentUrl()).isEqualTo(BASE_URL + "/fridges/foods/add?fridge.id=" + fridge.getId());
    }
    
    @Test
    public void clickDeleteFoodButton() {
        food.setFridge(fridge);
        FoodCommand foodCommand = createFood(food);
        Long id = foodCommand.getId();
        
        browser.get(BASE_URL + "/fridges/me");
        
        WebElement deleteAnchorElement = browser.findElements(By.tagName("a"))
            .stream()
            .filter(element -> {
                Pattern pattern = Pattern.compile(BASE_URL + "/fridges/foods/delete/[0-9]");
                Matcher matcher = pattern.matcher(element.getAttribute("href"));
                return matcher.find();
            })
            .findAny()
            .orElse(null);
        deleteAnchorElement.click();
        
        WebElement alertElement = browser.findElement(By.cssSelector("div.alert"));
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.visibilityOf(alertElement));
        
        assertThat(alertElement.getText()).isEqualTo("식품을 삭제했습니다.");
        
        ResponseEntity<FoodCommand> response = restTemplate.getForEntity(CORE_API_URL + "/foods/" + id, FoodCommand.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    
}
