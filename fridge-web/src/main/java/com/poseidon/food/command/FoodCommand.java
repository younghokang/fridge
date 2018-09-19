package com.poseidon.food.command;

import java.time.LocalDate;
import java.time.Period;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.poseidon.fridge.command.FridgeCommand;

public class FoodCommand {
    
    private Long id;
    
    @NotNull
    @Size(min=1, max=20)
    private String name;
    
    @Min(1)
    @Max(999)
    private Integer quantity;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate expiryDate;
    private FridgeCommand fridge;
    
    public static final int SHOW_EXPIRY_D_DAYS = -3;
    
    public FoodCommand() {}

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
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public FridgeCommand getFridge() {
        return fridge;
    }
    public void setFridge(FridgeCommand fridge) {
        this.fridge = fridge;
    }
    
    public int getExpiryDays() {
        return Period.between(LocalDate.now(), getExpiryDate()).getDays();
    }
    
    public String showExpiryDDay() {
        if(getExpiryDays() >= SHOW_EXPIRY_D_DAYS) {
            if(getExpiryDays() == 0) {
                return "D-Day";
            } else if(getExpiryDays() < 0) {
                return "D" + getExpiryDays();
            } else if(getExpiryDays() > 0) {
                return "D+" + getExpiryDays();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "FoodCommand [id=" + id + ", name=" + name + ", quantity=" + quantity + ", expiryDate=" + expiryDate
                + ", fridge=" + fridge + "]";
    }


}
