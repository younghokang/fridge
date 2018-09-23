package com.poseidon.fridge.model;

import java.util.List;

import com.poseidon.food.model.Food;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FridgeRequest {
    private Integer id;
    private String nickname;
    private List<Food> foods;
    private Long userId;
    
    public FridgeRequest(Fridge fridge) {
        this.id = fridge.getId();
        this.nickname = fridge.getNickname();
        this.userId = fridge.getUserId();
        this.foods = fridge.getFoods();
    }
    
    public Fridge toFridge() {
        return Fridge.builder()
                .id(id)
                .nickname(nickname)
                .userId(userId)
                .foods(foods)
                .build();
    }
    
}
