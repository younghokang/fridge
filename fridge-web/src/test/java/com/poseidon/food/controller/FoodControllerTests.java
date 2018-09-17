package com.poseidon.food.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.poseidon.ControllerBase;
import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

public class FoodControllerTests extends ControllerBase {
    private FridgeCommand fridge;
    private static final Long USER_ID = 1004L;
    private FoodCommand food = new FoodCommand("파스퇴르 우유 1.8L", 1, new Date());
    
    @Override
    protected void setUp() {
        fridge = createFridge("나의 냉장고", USER_ID);
        
        FoodCommand foodCommand = new FoodCommand("파스퇴르 우유 1.8L", 1, new Date());
        foodCommand.setFridge(fridge);
        food = createFood(foodCommand);
    }
    
    @Test
    public void fillInFoodRegisterFormAndSubmit() {
        browser.get(BASE_URL + "/fridges/foods/add?fridge.id=" + fridge.getId());
        
        LocalDateTime expiryDate = LocalDateTime.ofInstant(food.getExpiryDate().toInstant(), ZoneId.systemDefault());
        
        WebElement nameElement = browser.findElement(By.name("name"));
        WebElement quantityElement = browser.findElement(By.name("quantity"));
        WebElement expiryDateElement = browser.findElement(By.name("expiryDate"));
        nameElement.sendKeys(food.getName());
        quantityElement.sendKeys(Integer.toString(food.getQuantity()));
        expiryDateElement.sendKeys(expiryDate.format(DateTimeFormatter.ofPattern("yyyy")));
        expiryDateElement.sendKeys(Keys.TAB);
        expiryDateElement.sendKeys(expiryDate.format(DateTimeFormatter.ofPattern("MMdd")));
        browser.findElementByTagName("form").submit();
        
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        
        Alert alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("식품을 저장했습니다.");
        alert.accept();
    }
    
    @Test
    public void clickAnchorTagFromFood() {
        Long id = food.getId();
        
        browser.get(BASE_URL + "/fridges/me");
        
        String viewPageUrl = BASE_URL + "/fridges/foods/" + id;
        
        List<WebElement> anchors = browser.findElementsByLinkText(food.getName());
        assertThat(anchors).filteredOn(new Condition<WebElement>() {
            @Override
            public boolean matches(WebElement element) {
                return element.getAttribute("href").equals(viewPageUrl);
            }
        });
        
        WebElement anchorTag = anchors.stream()
                .filter(element -> element.getAttribute("href").equals(viewPageUrl))
                .findAny()
                .orElse(null);
        
        anchorTag.click();
        
        assertThat(browser.getCurrentUrl()).isEqualTo(viewPageUrl);
    }
    
    @Test
    public void changeFoodNameAndSubmit() {
        Long id = food.getId();
        
        browser.get(BASE_URL + "/fridges/foods/" + id);
        
        String changeName = "코카콜라 500mL";
        WebElement nameElement = browser.findElement(By.name("name"));
        nameElement.clear();
        nameElement.sendKeys(changeName);
        browser.findElementByTagName("form").submit();
        
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        
        Alert alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("식품을 저장했습니다.");
        alert.accept();
        
        browser.get(BASE_URL + "/fridges/foods/" + id);
        assertThat(browser.findElement(By.name("name")).getAttribute("value")).isEqualTo(changeName);
    }
    
}
