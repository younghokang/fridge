package com.poseidon.food.command;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.poseidon.fridge.command.FridgeCommand;

public class FoodCommand {
    
    private Long id;
    
    @NotNull
    @Size(max=20)
    private String name;
    
    @Min(1)
    @Max(999)
    private Integer quantity;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expiryDate;
    private FridgeCommand fridge;
    
    public FoodCommand() {}
    
    public FoodCommand(String name, Integer quantity, Date expiryDate) {
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
    public FridgeCommand getFridge() {
        return fridge;
    }
    public void setFridge(FridgeCommand fridge) {
        this.fridge = fridge;
    }

    @Override
    public String toString() {
        return "FoodCommand [id=" + id + ", name=" + name + ", quantity=" + quantity + ", expiryDate=" + expiryDate
                + "]";
    }
    
}
