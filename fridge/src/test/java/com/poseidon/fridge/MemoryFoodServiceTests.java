package com.poseidon.fridge;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.service.MemoryFoodService;

public class MemoryFoodServiceTests {
    
    private Food milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
    private Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
    private MemoryFoodService memoryFoodService;
    private List<Food> foods;
    
    @Before
    public void setUp() {
        memoryFoodService = new MemoryFoodService();
        foods = memoryFoodService.findAll();
    }
    
    @Test
    public void findAllAndSave() {
        assertThat(foods.size(), equalTo(0));
        
        memoryFoodService.save(milk);
        foods = memoryFoodService.findAll();
        assertThat(foods.contains(milk), equalTo(true));
    }
    
    @Test
    public void saveEquals() {
        memoryFoodService.save(milk);
        
        Food sameMilk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
        sameMilk.setId(milk.getId());
        memoryFoodService.save(sameMilk);
        foods = memoryFoodService.findAll();
        assertThat(foods.contains(sameMilk), equalTo(true));
        assertThat(foods.size(), equalTo(1));
        
        Food beer = new Food("카스 500mL", 6, "2019-09-01");
        beer.setId(sameMilk.getId());
        memoryFoodService.save(beer);
        foods = memoryFoodService.findAll();
        assertThat(foods.size(), equalTo(1));
        assertThat(foods.get(0).getName(), equalTo("카스 500mL"));
    }
    
    @Test
    public void findById() {
        memoryFoodService.save(milk);
        
        Food food = memoryFoodService.findById(milk.getId());
        assertThat(food, sameInstance(milk));
        
        Food nullFood = memoryFoodService.findById(999L);
        assertThat(nullFood, nullValue());
    }
    
    @Test
    public void immutableList_findAll() {
        Food beer = new Food("카스 500mL", 6, "2019-09-01");
        memoryFoodService.save(beer);
        foods = memoryFoodService.findAll();
        
        try {
            foods.add(cola);
            fail("ArrayList에 바로 접근하면 실패한다.");
        } catch(UnsupportedOperationException e) {
        }
        
        foods.get(0).decreaseQuantity(6);
        foods = memoryFoodService.findAll();
        assertThat(foods.get(0).getQuantity(), equalTo(6));
    }
    
    @Test
    public void modify() {
        Food beer = new Food("카스 500mL", 6, "2019-09-01");
        memoryFoodService.save(beer);
        beer.decreaseQuantity(5);
        assertThat(beer.getQuantity(), equalTo(1));
        
        memoryFoodService.save(beer);
        foods = memoryFoodService.findAll();
        Food first = foods.get(0);
        assertThat(first.getName(), equalTo("카스 500mL"));
        assertThat(first.getQuantity(), equalTo(1));
    }
    
    @Test
    public void remove() {
        Food beer = new Food("카스 500mL", 6, "2019-09-01");
        memoryFoodService.save(beer);
        boolean success = memoryFoodService.remove(beer);
        assertThat(success, equalTo(true));
        assertThat(memoryFoodService.findAll().size(), equalTo(0));
    }
    
    @Test
    public void removeById() {
        Long id = 1L;
        foods = Arrays.asList(milk, cola);
        for(Food food : foods) {
            memoryFoodService.save(food);
        }
        memoryFoodService.remove(id);
        foods = memoryFoodService.findAll();
        assertThat(foods.size(), equalTo(1));
    }

}
