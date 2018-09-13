package com.poseidon.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.poseidon.ControllerBase;

public class FridgeControllerTests extends ControllerBase {
    
    @Override
    protected void setUp() {
        
    }
    
    @Test
    public void create() {
        browser.get(BASE_URL + "/fridges");
        browser.findElementById("btnCreateFridge").click();
        
        String nickname = "myFridge";
        WebElement nicknameElement = browser.findElement(By.name("nickname"));
        nicknameElement.sendKeys(nickname);
        browser.findElementByTagName("form").submit();
        
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo(nickname + "을 생성했습니다.");
        alert.accept();
    }

}
