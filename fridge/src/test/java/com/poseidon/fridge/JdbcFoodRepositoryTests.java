package com.poseidon.fridge;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JdbcFoodRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcFoodRepositoryTests {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private JdbcFoodRepository jdbcFoodRepository;
    
    @Before
    public void setUp() {
        jdbcFoodRepository.deleteAll();
    }
    
    @Test
    public void connect() {
        int count = jdbcTemplate.queryForObject("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", int.class);
        assertThat(count, equalTo(1));
    }
    
    @Test
    public void insert() {
        Food milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
        int affectedRows = jdbcFoodRepository.insert(milk);
        assertThat(affectedRows, equalTo(1));
        assertThat(jdbcFoodRepository.count(), equalTo(1));
        
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        affectedRows = jdbcFoodRepository.insert(cola);
        
        assertThat(affectedRows, equalTo(1));
        assertThat(jdbcFoodRepository.count(), equalTo(2));
        assertThat(cola.getId(), notNullValue());
    }
    
    @Test
    public void delete_all() {
        jdbcFoodRepository.deleteAll();
        assertThat(jdbcFoodRepository.count(), equalTo(0));
    }
    
    @Test
    public void select_all() {
        List<Food> foods = Arrays.asList(
                new Food("파스퇴르 우유 1.8L", 1, "2018-09-28"),
                new Food("코카콜라 500mL", 2, "2018-10-30")
        );
        for(Food food : foods) {
            jdbcFoodRepository.insert(food);
        }
        List<Food> storeFoods = jdbcFoodRepository.findAll();
        assertThat(storeFoods.size(), equalTo(2));
        assertThat(storeFoods.get(0).getName(), equalTo(foods.get(0).getName()));
    }

    @Test
    public void selectOne() {
        Food milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
        int affectedRows = jdbcFoodRepository.insert(milk);
        assertThat(affectedRows, equalTo(1));
        
        Food storeFood = jdbcFoodRepository.findById(milk.getId());
        assertThat(storeFood.getId(), equalTo(milk.getId()));
    }
    
    @Test
    public void updateFood() {
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        int affectedRows = jdbcFoodRepository.insert(cola);
        assertThat(affectedRows, equalTo(1));
        
        cola.decreaseQuantity(1);
        assertThat(cola.getQuantity(), equalTo(1));
        
        affectedRows = jdbcFoodRepository.update(cola);
        assertThat(affectedRows, equalTo(1));
        
        Food storeFood = jdbcFoodRepository.findById(cola.getId());
        assertThat(storeFood.getQuantity(), equalTo(cola.getQuantity()));
    }

}
