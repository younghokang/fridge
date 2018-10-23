package com.poseidon.food.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.fridge.model.Fridge;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Food {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    
    @ManyToOne
    private Fridge fridge;
    
    private @CreatedDate LocalDateTime createdDate;
    private @LastModifiedDate LocalDateTime lastModifiedDate;
    
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
