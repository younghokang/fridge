package com.poseidon.food.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.poseidon.common.BaseEntity;
import com.poseidon.fridge.model.Fridge;

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
public class Food extends BaseEntity {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    
    @ManyToOne
    private Fridge fridge;
    private Long categoryId;
    
    @Builder
    public Food(Long id, String name, int quantity, LocalDate expiryDate, Fridge fridge) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.fridge = fridge;
    }
    
    public Integer getFridgeId() {
        return fridge.getId();
    }
    public void setFridgeId(Integer fridgeId) {
        if(fridge == null) {
            fridge = new Fridge();
        }
        fridge.setId(fridgeId);
    }
    
}
