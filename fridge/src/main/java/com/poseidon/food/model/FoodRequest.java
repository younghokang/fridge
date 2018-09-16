package com.poseidon.food.model;

import java.util.Date;

import com.poseidon.fridge.model.Fridge;

public class FoodRequest {
    private Long id;
    private String name;
    private Integer quantity;
    private Date expiryDate;
    private Fridge fridge;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Fridge getFridge() {
        return fridge;
    }
    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }
    
    public Food toFood() {
        Food food = new Food(getName(), 
                getQuantity(), 
                getExpiryDate());
        food.setId(getId());
        food.setFridge(getFridge());
        return food;
    }
    
    @Override
    public String toString() {
        return "FoodRequest [id=" + id + ", name=" + name + ", quantity=" + quantity + ", expiryDate=" + expiryDate
                + ", fridge=" + fridge + "]";
    }

}
