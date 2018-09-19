package com.poseidon.food.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poseidon.fridge.model.Fridge;

@Entity(name="food")
public class Food {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JsonIgnore
    private Fridge fridge;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    
    public static final int DEFAULT_EXPIRY_DAYS = 7;
    
    protected Food() {}
    
    public Long getId() {
        return id;
    }
    public Fridge getFridge() {
        return fridge;
    }
    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    private void setDefaultExpiryDate() {
        this.expiryDate = LocalDate.now().plusDays(DEFAULT_EXPIRY_DAYS);
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
        Food other = (Food) obj;
        return Objects.equals(getId(), other.getId());
    }
    
    public static class Builder {
        private Long id;
        private final String name;
        private final int quantity;
        private LocalDate expiryDate;
        private Fridge fridge;
        
        public Builder(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }
        
        public Builder fridge(Fridge fridge) {
            this.fridge = fridge;
            return this;
        }
        
        public Food build() {
            return new Food(this);
        }
    }
    
    private Food(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.expiryDate = builder.expiryDate;
        if(this.expiryDate == null) {
            setDefaultExpiryDate();
        }
        this.fridge = builder.fridge;
    }

}
