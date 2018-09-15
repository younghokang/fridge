package com.poseidon.food.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
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
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Food cola;
    
    @Before
    public void setUp() throws ParseException {
        cola = new Food("코카콜라 500mL", 2, sdf.parse("2018-10-30"));
    }
    
    @Test
    public void save() {
        jpaFoodRepository.save(cola);
        Food food = jpaFoodRepository.findOne(cola.getId());
        assertThat(food.getName()).isEqualTo(cola.getName());
    }
    
    @Test
    public void update() {
        jpaFoodRepository.save(cola);
        
        cola.decreaseQuantity(1);
        jpaFoodRepository.flush();
        Food savedCola = jpaFoodRepository.findOne(cola.getId());
        assertThat(savedCola.getQuantity()).isEqualTo(1);
    }
    
    @Test
    public void remove() {
        jpaFoodRepository.save(cola);
        assertThat(jpaFoodRepository.findAll().size()).isEqualTo(1);
        
        jpaFoodRepository.delete(cola.getId());
        assertThat(jpaFoodRepository.findAll().size()).isEqualTo(0);
    }
    
}
