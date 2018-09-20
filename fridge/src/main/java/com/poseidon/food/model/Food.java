package com.poseidon.food.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poseidon.fridge.model.Fridge;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Food {
    private @Id @GeneratedValue Long id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    
    @ManyToOne
    @JsonIgnore
    private Fridge fridge;
    
    @Builder
    public Food(Long id, String name, int quantity, LocalDate expiryDate, Fridge fridge) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        if(expiryDate == null) {
            setDefaultExpiryDate();
        }
        this.fridge = fridge;
    }
    
    public static final int DEFAULT_EXPIRY_DAYS = 7;
    
    private void setDefaultExpiryDate() {
        this.expiryDate = LocalDate.now().plusDays(DEFAULT_EXPIRY_DAYS);
    }
    
}
