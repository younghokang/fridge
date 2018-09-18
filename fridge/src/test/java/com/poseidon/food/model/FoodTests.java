package com.poseidon.food.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;

import org.junit.Test;

public class FoodTests {

    @Test
    public void createBuilder() throws ParseException {
        Food milk = new Food.Builder("파스퇴르 우유 1.8L", 1)
                .expiryDate(LocalDate.of(2018, 9, 28)).build();
        
        assertThat(milk.getName()).isEqualTo("파스퇴르 우유 1.8L");
        assertThat(milk.getQuantity()).isEqualTo(1);
        assertThat(milk.getExpiryDate().toString()).isEqualTo("2018-09-28");
        
        Food coke = new Food.Builder("코카콜라 500mL", 2)
                .expiryDate(LocalDate.of(2018, 10, 30))
                .build();
        
        assertThat(coke.getName()).isEqualTo("코카콜라 500mL");
        assertThat(coke.getQuantity()).isGreaterThanOrEqualTo(2);
        assertThat(coke.getExpiryDate().toString()).isEqualTo("2018-10-30");
    }

    @Test
    public void createBuilderWithoutExpiryDate() {
        Food milk = new Food.Builder("파스퇴르 우유 1.8L", 1).build();
        assertThat(milk.getExpiryDate()).isNotNull();
        assertThat(milk.getExpiryDate()).isGreaterThan(LocalDate.now());
        assertThat(milk.getExpiryDate()).isEqualTo(LocalDate.now().plusDays(Food.DEFAULT_EXPIRY_DAYS));
    }
    
}
