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
public class JpaFoodRepositoryTests {
    
    @Autowired
    JpaFoodRepository jpaFoodRepository;
    
    private Food cola = new Food.Builder("코카콜라 500mL", 2)
            .expiryDate(LocalDate.of(2018, 9, 10))
            .build();
    
    @Test
    public void save() {
        jpaFoodRepository.save(cola);
        Food food = jpaFoodRepository.findOne(cola.getId());
        assertThat(food.getName()).isEqualTo(cola.getName());
        assertThat(food.getExpiryDate()).isEqualByComparingTo(LocalDate.of(2018, 9, 10));
    }
    
    @Test
    public void remove() {
        jpaFoodRepository.save(cola);
        assertThat(jpaFoodRepository.findAll().size()).isEqualTo(1);
        
        jpaFoodRepository.delete(cola.getId());
        assertThat(jpaFoodRepository.findAll().size()).isEqualTo(0);
    }
    
}
