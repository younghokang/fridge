package com.poseidon.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

public class FridgeControllerTests extends ControllerBase {
    private FridgeCommand fridge;
    
    @Override
    protected void setUp() {
        fridge = createFridge("나의 냉장고");
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
    
    private FoodCommand food = new FoodCommand("파스퇴르 우유 1.8L", 1, new Date());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @Test
    public void hasContentsFromFridgesTable() {
        food.setFridge(fridge);
        FoodCommand foodCommand = createFood(food);
        
        browser.get(BASE_URL + "/fridges");
        assertThat(browser.findElement(By.cssSelector("a.nav-link.active")).getText()).isEqualTo(fridge.getNickname());
        
        WebElement table = browser.findElement(By.tagName("table"));
        assertThat(table.isDisplayed()).isTrue();
        
        List<WebElement> tr = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        assertThat(tr.size()).isEqualTo(1);
        
        List<WebElement> td = tr.get(0).findElements(By.tagName("td"));
        assertThat(td.get(0).getText()).isEqualTo("1");
        assertThat(td.get(1).findElement(By.tagName("a")).getText()).isEqualTo(foodCommand.getName());
        assertThat(td.get(2).getText()).isEqualTo(Integer.toString(foodCommand.getQuantity()));
        assertThat(td.get(3).getText()).isEqualTo(sdf.format(foodCommand.getExpiryDate()));
        assertThat(td.get(4).findElement(By.tagName("a")).getAttribute("href")).containsPattern(Pattern.compile(BASE_URL + "/foods/delete/[0-9]"));
    }
    
}
