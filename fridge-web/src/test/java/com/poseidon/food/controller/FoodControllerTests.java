package com.poseidon.food.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.poseidon.ControllerBase;

public class FoodControllerTests extends ControllerBase {
    
    @Test
    public void registerFood() {
        String name = "파스퇴르 우유 1.8L";
        int quantity = 1;
        
        browser.get(BASE_URL + "/foods");
        browser.findElement(By.id("btnRegistrationFood")).click();
        
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
        alert.accept();
        
        List<WebElement> anchorElements = browser.findElementByTagName("table").findElements(By.tagName("a"));
        WebElement anchorElement = anchorElements.get(anchorElements.size() - 1);
        assertThat(anchorElement.getText()).isEqualTo(name);
        anchorElement.click();
        
        assertThat(browser.findElement(By.name("name")).getAttribute("value")).isEqualTo(name);
        nameElement = browser.findElement(By.name("name"));
        nameElement.clear();
        nameElement.sendKeys("코카콜라 500mL");
        
        quantityElement = browser.findElement(By.name("quantity"));
        quantityElement.clear();
        quantityElement.sendKeys("20");
        browser.findElementByTagName("form").submit();
        
        wait.until(ExpectedConditions.alertIsPresent());
        alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("식품을 저장했습니다.");
        alert.accept();
        
        browser.findElement(By.linkText("코카콜라 500mL")).click();
        browser.findElement(By.linkText("삭제")).click();
        
        wait.until(ExpectedConditions.alertIsPresent());
        alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("식품을 삭제했습니다.");
        alert.accept();
    }

}
