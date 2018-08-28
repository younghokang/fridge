package com.poseidon.fridge;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JpaFoodRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaFoodRepositoryTests {
    
    @Autowired
    JpaFoodRepository jpaFoodRepository;
    
    @Test
    public void save() {
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        jpaFoodRepository.save(cola);
        Food food = jpaFoodRepository.findOne(cola.getId());
        assertThat(food.getName()).isEqualTo(cola.getName());
    }
    
    @Test
    public void update() {
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        jpaFoodRepository.save(cola);
        
        cola.decreaseQuantity(1);
        jpaFoodRepository.flush();
        Food savedCola = jpaFoodRepository.findOne(cola.getId());
        assertThat(savedCola.getQuantity()).isEqualTo(1);
    }
    
    @Test
    public void remove() {
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        jpaFoodRepository.save(cola);
        assertThat(jpaFoodRepository.findAll().size()).isEqualTo(1);
        
        jpaFoodRepository.delete(cola.getId());
        assertThat(jpaFoodRepository.findAll().size()).isEqualTo(0);
    }
    
}
