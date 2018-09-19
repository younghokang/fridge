package com.poseidon.food.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import com.poseidon.food.command.FoodCommand;

public class FoodCommandTests {
    
    @Test
    public void showExpiryDDay() {
        FoodCommand foodCommand = new FoodCommand();
        foodCommand.setExpiryDate(LocalDate.now());
        assertThat(foodCommand.showExpiryDDay()).isEqualTo("D-Day");
        
        foodCommand.setExpiryDate(LocalDate.now().minusDays(3));
        assertThat(foodCommand.showExpiryDDay()).isEqualTo("D-3");
        
        foodCommand.setExpiryDate(LocalDate.now().minusDays(4));
        assertThat(foodCommand.showExpiryDDay()).isNull();
        
        foodCommand.setExpiryDate(LocalDate.now().plusDays(1));
        assertThat(foodCommand.showExpiryDDay()).isEqualTo("D+1");
    }

}
