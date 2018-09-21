package com.poseidon.fridge.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.poseidon.food.model.Food;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Fridge {
    private @Id @GeneratedValue Integer id;
    private String nickname;
    private Long userId;
    
    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();
    
    @Builder
    public Fridge(Integer id, String nickname, Long userId, List<Food> foods) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
        if(foods == null) {
            this.foods = new ArrayList<>();
        }
    }
    
    public void addFood(Food food) {
        this.foods.add(food);
        food.setFridge(this);
    }

    public void removeFood(Food food) {
        this.foods.remove(food);
        food.setFridge(null);
    }
    
}
