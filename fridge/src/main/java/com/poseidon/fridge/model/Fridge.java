package com.poseidon.fridge.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.poseidon.common.BaseEntity;
import com.poseidon.food.model.Food;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Fridge extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    private Long userId;
    
    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();
    
    @Builder
    public Fridge(Integer id, String nickname, Long userId, List<Food> foods) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
        this.foods = new ArrayList<>();
        if(foods != null) {
            this.foods.addAll(foods);
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
