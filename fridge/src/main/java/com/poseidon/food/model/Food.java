package com.poseidon.food.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    
    public Food() {}
    
    public Food(String name, int quantity, Date expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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

    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void decreaseQuantity(int quantity) {
        if (quantity < 0 || this.quantity - quantity < 0) {
            throw new IllegalArgumentException("current(" + this.quantity + "), quantity(" + quantity + ")");
        }
        this.quantity -= quantity;
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

}
