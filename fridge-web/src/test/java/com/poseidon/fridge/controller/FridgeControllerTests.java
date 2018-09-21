package com.poseidon.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.poseidon.ControllerBase;
import com.poseidon.food.command.FoodCommand;
import com.poseidon.food.service.FoodRestService;
import com.poseidon.fridge.command.FridgeCommand;

public class FridgeControllerTests extends ControllerBase {
    private FridgeCommand fridge;
    private FoodCommand food;
    
    @Autowired
    private FoodRestService service;
    
    @Override
    protected void setUp() {
        fridge = createFridge("나의 냉장고", 1004L);
        
        FoodCommand foodCommand = FoodCommand.builder()
                .name("파스퇴르 우유 1.8L")
                .quantity(1)
                .expiryDate(LocalDate.now())
                .fridgeId(fridge.getId())
                .build();
        food = createFood(foodCommand);
    }
    
    @Test
    public void hasContentsFromFridgesTable() {
        browser.get(BASE_URL + "/fridges/me");
        WebElement table = browser.findElement(By.tagName("table"));
        assertThat(table.isDisplayed()).isTrue();
        
        List<WebElement> tr = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        assertThat(tr.size()).isEqualTo(1);
        
        List<WebElement> td = tr.get(0).findElements(By.tagName("td"));
        assertThat(td.get(0).getText()).isEqualTo("1");
        assertThat(td.get(1).findElement(By.tagName("a")).getText()).isEqualTo(food.getName());
        assertThat(td.get(2).getText()).isEqualTo(Integer.toString(food.getQuantity()));
        assertThat(td.get(3).getText()).isEqualTo(food.showExpiryDDay());
        assertThat(td.get(4).findElement(By.tagName("a")).getAttribute("href")).contains("/fridges/" + fridge.getId() + "/foods/delete/" + food.getId());
    }
    
    @Test
    public void findRegistrationFoodButtonAndClick() {
        browser.get(BASE_URL + "/fridges/me");
        browser.findElement(By.id("btnRegistrationFood")).click();
        assertThat(browser.getCurrentUrl()).contains("/fridges/" + fridge.getId() + "/foods/add");
    }
    
    @Test
    public void clickDeleteFoodButton() {
        browser.get(BASE_URL + "/fridges/me");
        
        WebElement deleteAnchorElement = browser.findElements(By.tagName("a"))
            .stream()
            .filter(element -> {
                return element.getAttribute("href").contains("/fridges/" + fridge.getId() + "/foods/delete/" + food.getId());
            })
            .findAny()
            .orElse(null);
        deleteAnchorElement.click();
        
        WebElement alertElement = browser.findElement(By.cssSelector("div.alert"));
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.visibilityOf(alertElement));
        
        assertThat(alertElement.getText()).isEqualTo("식품을 삭제했습니다.");
        assertThat(service.loadById(food.getId())).isNull();
    }
    
}
