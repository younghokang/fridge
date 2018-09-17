package com.poseidon.fridge.model;

import java.util.List;

import com.poseidon.food.model.Food;

public class FridgeRequest {
    private Integer id;
    private String nickname;
    private List<Food> foods;
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
    public List<Food> getFoods() {
        return foods;
    }
    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Fridge toFridge() {
        Fridge fridge = new Fridge(getNickname(), getFoods());
        fridge.setId(getId());
        fridge.setUserId(getUserId());
        return fridge;
    }
    @Override
    public String toString() {
        return "FridgeRequest [id=" + id + ", nickname=" + nickname + ", foods=" + foods + ", userId=" + userId + "]";
    }
}
