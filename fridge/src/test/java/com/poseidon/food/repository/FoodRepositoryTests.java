package com.poseidon.food.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.food.model.Food;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FoodRepositoryTests {
    
    @Autowired
    FoodRepository repository;
    
    private Food cola = Food.builder()
            .name("코카콜라 500mL")
            .quantity(2)
            .expiryDate(LocalDate.of(2018, 9, 10))
            .build();
    
    @Test
    public void save() {
        repository.save(cola);
        Food food = repository.findOne(cola.getId());
        assertThat(food.getName()).isEqualTo(cola.getName());
        assertThat(food.getExpiryDate()).isEqualByComparingTo(LocalDate.of(2018, 9, 10));
    }
    
    @Test
    public void remove() {
        repository.save(cola);
        assertThat(repository.findAll().size()).isEqualTo(1);
        
        repository.delete(cola.getId());
        assertThat(repository.findAll().size()).isEqualTo(0);
    }
    
}
