package com.poseidon.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.poseidon.ControllerBase;
import com.poseidon.fridge.command.FridgeCommand;

public class FridgeControllerTests extends ControllerBase {
    private FridgeCommand fridge;
    
    @Override
    protected void setUp() {
        restTemplate.delete(CORE_API_URL + "/fridges", Collections.emptyMap());
        fridge = createFridge("myFridge");
    }
    
    private FridgeCommand createFridge(String nickname) {
        ResponseEntity<FridgeCommand> response = restTemplate.postForEntity(CORE_API_URL + "/fridges", nickname, FridgeCommand.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        FridgeCommand fridge = response.getBody();
        assertThat(fridge.getId()).isPositive();
        return fridge;
    }
    
    @Test
    public void findCreateFridgeButtonAndClick() {
        browser.get(BASE_URL + "/fridges");
        WebElement createFridgeBtn = browser.findElementById("btnCreateFridge");
        assertThat(createFridgeBtn).isNotNull();
        
        createFridgeBtn.click();
        assertThat(browser.getCurrentUrl()).isEqualTo(BASE_URL + "/fridges/add");
    }
    
    @Test
    public void fillInCreateFormAndSubmit() {
        browser.get(BASE_URL + "/fridges/add");
        
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
    
    @Test
    public void clickAnchorTagFromFridges() {
        browser.get(BASE_URL + "/fridges");
        
        String viewPageUrl = BASE_URL + "/fridges/" + fridge.getId();
        
        List<WebElement> anchors = browser.findElementsByLinkText(fridge.getNickname());
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
    public void changeNickNameAndSubmit() {
        browser.get(BASE_URL + "/fridges/" + fridge.getId());
        
        String changeNickname = "otherFridge";
        WebElement nicknameElement = browser.findElement(By.name("nickname"));
        assertThat(nicknameElement.getAttribute("value")).isEqualTo(fridge.getNickname());
        
        nicknameElement.clear();
        nicknameElement.sendKeys(changeNickname);
        browser.findElementByTagName("form").submit();
        
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        
        Alert alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo(changeNickname + "을 수정했습니다.");
        alert.accept();
        
        browser.get(BASE_URL + "/fridges/" + fridge.getId());
        assertThat(browser.findElement(By.name("nickname")).getAttribute("value")).isEqualTo(changeNickname);
    }
    
    @Test
    public void clickDeleteFridgeButton() {
        browser.get(BASE_URL + "/fridges/" + fridge.getId());
        
        WebElement deleteBtn = browser.findElement(By.linkText("삭제"));
        assertThat(deleteBtn.getAttribute("href")).isEqualTo(BASE_URL + "/fridges/delete/" + fridge.getId());
        deleteBtn.click();
        
        WebDriverWait wait = new WebDriverWait(browser, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = browser.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("삭제했습니다.");
        alert.accept();
        
        ResponseEntity<FridgeCommand> response = restTemplate.getForEntity(CORE_API_URL + "/fridges/" + fridge.getId(), FridgeCommand.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
