package com.poseidon.food.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.food.model.Food;
import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.repository.JpaFridgeRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaFoodRepositoryTests {
    
    @Autowired
    JpaFoodRepository repository;
    
    @Autowired
    JpaFridgeRepository fridgeRepository;
    
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
    
    @Test
    public void findAllByFridge() {
        Fridge myFridge = Fridge.builder()
            .nickname("myFridge")
            .build();
        fridgeRepository.save(myFridge);
        
        Fridge secondFridge = Fridge.builder()
                .nickname("second")
                .build();
        fridgeRepository.save(secondFridge);
        
        List<Food> foods = Arrays.asList(
                Food.builder().name("Banana").quantity(3).fridge(myFridge).build(),
                Food.builder().name("Apple").quantity(3).fridge(myFridge).build(),
                Food.builder().name("Orange").quantity(3).fridge(myFridge).build(),
                Food.builder().name("Milk").quantity(3).fridge(secondFridge).build());
        repository.save(foods);
        
        List<Food> myFridgeFoods = repository.findAllByFridgeId(myFridge.getId());
        assertThat(myFridgeFoods.size()).isEqualTo(3);
    }
    
}
