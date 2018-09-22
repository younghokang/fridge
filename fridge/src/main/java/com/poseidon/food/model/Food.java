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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poseidon.fridge.model.Fridge;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
    @JsonIgnore
    private Fridge fridge;
    
    private @CreatedDate LocalDateTime createdDate;
    private @LastModifiedDate LocalDateTime lastModifiedDate;
    
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
