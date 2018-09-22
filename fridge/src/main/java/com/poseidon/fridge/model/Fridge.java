package com.poseidon.fridge.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.food.model.Food;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Fridge {
    private @Id @GeneratedValue Integer id;
    private String nickname;
    private Long userId;
    
    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();
    
    private @CreatedDate LocalDateTime createdDate;
    private @LastModifiedDate LocalDateTime lastModifiedDate;
    
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
