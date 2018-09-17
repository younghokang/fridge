package com.poseidon.fridge.command;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.poseidon.food.command.FoodCommand;

public class FridgeCommand {
    private Integer id;
    @NotNull
    @Size(min=2, max=15)
    private String nickname;
    private List<FoodCommand> foods;
    private Long userId;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public List<FoodCommand> getFoods() {
        return foods;
    }
    public void setFoods(List<FoodCommand> foods) {
        this.foods = foods;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
