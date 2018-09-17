package com.poseidon.fridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poseidon.food.model.Food;

@Entity(name="fridge")
public class Fridge {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();
    
    private Long userId;
    
    protected Fridge() {}
    
    public Fridge(String nickname) {
        this(nickname, Collections.emptyList());
    }
    
    public Fridge(String nickname, List<Food> foods) {
        this.nickname = nickname;
        this.foods.addAll(foods);
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNickname() {
        return nickname;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public boolean hasFood() {
        return !foods.isEmpty();
    }

    public void addFood(Food food) {
        this.foods.add(food);
        food.setFridge(this);
    }

    public void removeFood(Food food) {
        this.foods.remove(food);
        food.setFridge(null);
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Fridge other = (Fridge) obj;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public String toString() {
        return "Fridge [id=" + id + ", nickname=" + nickname + ", userId=" + userId + "]";
    }

}
