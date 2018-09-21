package com.poseidon.fridge.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.poseidon.food.model.Food;

public class FridgeTests {
    
    @Test
    public void newFridge() {
        String nickname = "myFridge";
        Fridge fridge = Fridge.builder()
                .nickname(nickname)
                .build();
        assertThat(fridge).isNotNull();
        assertThat(fridge.getNickname()).isEqualTo(nickname);
        assertThat(fridge.getNickname()).isNotEqualTo("Blah blah");
    }
    
    @Test
    public void newFridgeWithFoods() {
        Fridge fridge = Fridge.builder()
                .nickname("myFridge")
                .build();
        
        List<Food> foods = fridge.getFoods();
        fridge.addFood(Food.builder().name("파스퇴르 우유 1.8L").quantity(1).build());
        
        assertThat(fridge.getFoods()).containsOnlyElementsOf(foods);
        assertThat(fridge.getFoods().size()).isEqualTo(1);
        
        Food coke = Food.builder()
                .name("코카콜라 500mL")
                .quantity(2)
                .build();
        
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
        Fridge firstFridge = Fridge.builder()
                .nickname("firstFridge")
                .userId(firstUserId)
                .build();
        assertThat(firstFridge.getUserId()).isEqualTo(firstUserId);
        
        long secondUserId = 1008L;
        Fridge secondFridge = Fridge.builder()
                .nickname("secondFridge")
                .userId(secondUserId)
                .build();
        secondFridge.setUserId(secondUserId);
        assertThat(secondFridge.getUserId()).isNotEqualTo(firstUserId);
        assertThat(secondFridge.getUserId()).isEqualTo(secondUserId);
    }

}
