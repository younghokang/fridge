package com.poseidon.food.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.util.UriComponentsBuilder;

import com.poseidon.ControllerBase;
import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

public class FoodControllerTests extends ControllerBase {
    private FridgeCommand fridge;
    private FoodCommand food;
    
    @Override
    protected void setUp() {
        fridge = createFridge("나의 냉장고", 1004L);
        FoodCommand foodCommand = FoodCommand.builder()
                .name("파스퇴르 우유 1.8L")
                .quantity(1)
                .fridgeId(fridge.getId())
                .build();
        food = createFood(foodCommand);
    }
    
    @Test
    public void fillInFoodRegisterFormAndSubmit() {
        assertThat(food.getId()).isPositive();
        
        browser.get(UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/fridges/{fridgeId}/foods/add")
                .buildAndExpand(fridge.getId())
                .toString());
        
        WebElement nameElement = browser.findElement(By.name("name"));
        WebElement quantityElement = browser.findElement(By.name("quantity"));
        WebElement expiryDateElement = browser.findElement(By.name("expiryDate"));
        nameElement.sendKeys(food.getName());
        quantityElement.sendKeys(Integer.toString(food.getQuantity()));
        expiryDateElement.sendKeys(food.getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy")));
        expiryDateElement.sendKeys(Keys.TAB);
        expiryDateElement.sendKeys(food.getExpiryDate().format(DateTimeFormatter.ofPattern("MMdd")));
        browser.findElementByTagName("form").submit();
        
        WebElement alertElement = browser.findElement(By.cssSelector("div.alert"));
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.visibilityOf(alertElement));
        
        assertThat(alertElement.getText()).isEqualTo("식품을 저장했습니다.");
    }
    
    @Test
    public void clickAnchorTagFromFood() {
        Long id = food.getId();
        
        browser.get(BASE_URL + "/fridges/me");
        
        String viewPageUrl = BASE_URL + "/fridges/" + fridge.getId() + "/foods/" + id;
        
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
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/fridges/{fridgeId}/foods/{id}")
                .buildAndExpand(fridge.getId(), food.getId())
                .toString();
        browser.get(url);
        
        String changeName = "코카콜라 500mL";
        WebElement nameElement = browser.findElement(By.name("name"));
        nameElement.clear();
        nameElement.sendKeys(changeName);
        browser.findElementByTagName("form").submit();
        
        WebElement alertElement = browser.findElement(By.cssSelector("div.alert"));
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.visibilityOf(alertElement));
        
        assertThat(alertElement.getText()).isEqualTo("식품을 저장했습니다.");
        
        browser.get(url);
        assertThat(browser.findElement(By.name("name")).getAttribute("value")).isEqualTo(changeName);
    }
    
}
