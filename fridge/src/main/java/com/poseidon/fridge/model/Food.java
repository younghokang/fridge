package com.poseidon.fridge.model;

import com.google.common.base.Objects;

public class Food implements Cloneable {
    private Long id;
    private String name;
    private int quantity;
    private String expiryDate;
    
    public Food(String name, int quantity, String expiryDate) {
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

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
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
        return java.util.Objects.equals(getId(), other.getId());
    }

    @Override
    public Food clone() {
        try {
            return (Food)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
