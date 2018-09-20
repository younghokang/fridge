package com.poseidon.food.model;

import java.time.LocalDate;

import com.poseidon.fridge.model.Fridge;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodRequest {
    private Long id;
    private String name;
    private Integer quantity;
    private LocalDate expiryDate;
    private Fridge fridge;
    
    public FoodRequest(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.quantity = food.getQuantity();
        this.expiryDate = food.getExpiryDate();
        this.fridge = food.getFridge();
    }
    
    public Food toFood() {
        return Food.builder()
                .id(id)
                .name(name)
                .quantity(quantity)
                .expiryDate(expiryDate)
                .fridge(fridge)
                .build();
    }
    
}
