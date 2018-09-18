package com.poseidon.fridge.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.poseidon.food.model.Food;

public class FridgeTests {
    
    @Test
    public void newFridge() {
        String nickname = "myFridge";
        Fridge myFridge = new Fridge(nickname);
        assertThat(myFridge).isNotNull();
        assertThat(myFridge.getNickname()).isEqualTo(nickname);
        assertThat(myFridge.getNickname()).isNotEqualTo("Blah blah");
    }
    
    @Test
    public void newFridgeWithFoods() {
        String nickname = "myFridge";
        List<Food> foods = Arrays.asList(new Food.Builder("파스퇴르 우유 1.8L", 1).build());
        Fridge fridge = new Fridge(nickname, foods);
        assertThat(fridge.getFoods()).containsOnlyElementsOf(foods);
        assertThat(fridge.hasFood()).isTrue();
        assertThat(fridge.getFoods().size()).isEqualTo(1);
        
        Food coke = new Food.Builder("코카콜라 500mL", 2).build();
        fridge.addFood(coke);
        assertThat(fridge.getFoods().size()).isGreaterThanOrEqualTo(2);
        assertThat(fridge.getFoods()).contains(coke);
        
        fridge.removeFood(coke);
        assertThat(fridge.getFoods().size()).isEqualTo(1);
        assertThat(fridge.getFoods()).containsOnlyElementsOf(foods);
    }
    
    @Test
    public void fridgeByUserId() {
        long firstUserId = 1004L;
        Fridge firstFridge = new Fridge("firstFridge");
        firstFridge.setUserId(firstUserId);
        assertThat(firstFridge.getUserId()).isEqualTo(firstUserId);
        
        long secondUserId = 1008L;
        Fridge secondFridge = new Fridge("secondFridge");
        secondFridge.setUserId(secondUserId);
        assertThat(secondFridge.getUserId()).isNotEqualTo(firstUserId);
        assertThat(secondFridge.getUserId()).isEqualTo(secondUserId);
    }

}
