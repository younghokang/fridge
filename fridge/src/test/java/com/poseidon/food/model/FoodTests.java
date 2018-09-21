package com.poseidon.food.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;

import org.junit.Test;

public class FoodTests {

    @Test
    public void createBuilder() throws ParseException {
        Food milk = Food.builder()
                .name("파스퇴르 우유 1.8L")
                .quantity(1)
                .expiryDate(LocalDate.of(2018, 9, 28))
                .build();
        
        assertThat(milk.getName()).isEqualTo("파스퇴르 우유 1.8L");
        assertThat(milk.getQuantity()).isEqualTo(1);
        assertThat(milk.getExpiryDate().toString()).isEqualTo("2018-09-28");
        
        Food coke = Food.builder()
                .name("코카콜라 500mL")
                .quantity(2)
                .expiryDate(LocalDate.of(2018, 10, 30))
                .build();
        
        assertThat(coke.getName()).isEqualTo("코카콜라 500mL");
        assertThat(coke.getQuantity()).isGreaterThanOrEqualTo(2);
        assertThat(coke.getExpiryDate().toString()).isEqualTo("2018-10-30");
    }

    @Test
    public void createBuilderWithoutExpiryDate() {
        Food food = Food.builder()
                .name("파스퇴르 우유 1.8L")
                .quantity(1)
                .build();
        
        assertThat(food.getExpiryDate()).isNotNull();
        assertThat(food.getExpiryDate()).isGreaterThan(LocalDate.now());
        assertThat(food.getExpiryDate()).isEqualTo(LocalDate.now().plusDays(Food.DEFAULT_EXPIRY_DAYS));
    }
    
}
