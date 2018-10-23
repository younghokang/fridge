package com.poseidon.fridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.food.command.Food;
import com.poseidon.fridge.command.Fridge;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK, properties= {"eureka.client.enabled:false"})
public class FridgeClientTests {
    
    @MockBean
    FridgeClient client;
    
    private Fridge fridgeCommand = Fridge.builder()
            .id(1)
            .nickname("myFridge")
            .userId(1004L)
            .build();
    
    @Test
    public void loadByUserId() {
        when(client.loadByUserId(anyLong())).thenReturn(fridgeCommand);
        Fridge fridge = client.loadByUserId(fridgeCommand.getUserId());
        assertThat(fridge).isEqualToComparingFieldByField(fridgeCommand);
    }
    
    @Test(expected=NotFoundException.class)
    public void whenLoadByUserIdThenNotFoundException() {
        when(client.loadByUserId(anyLong())).thenThrow(NotFoundException.class);
        client.loadByUserId(fridgeCommand.getUserId());
    }
    
    @Test
    public void generate() {
        when(client.generate(any(Fridge.class))).thenReturn(fridgeCommand);
        Fridge fridge = client.generate(fridgeCommand);
        assertThat(fridge).isEqualToComparingFieldByField(fridgeCommand);
    }
    
    @Test
    public void deleteAll() {
        doNothing().when(client).deleteAll();
        client.deleteAll();
        verify(client, times(1)).deleteAll();
    }
    
    private Food food = Food.builder()
            .id(1L)
            .name("Banana Cake")
            .quantity(3)
            .expiryDate(LocalDate.now())
            .fridgeId(1)
            .build();
    
    @Test
    public void createFood() {
        when(client.createFood(any(Food.class))).thenReturn(food);
        Food newFood = client.createFood(food);
        assertThat(newFood).isEqualToComparingFieldByFieldRecursively(food);
    }
    
    @Test
    public void loadFoodById() {
        when(client.loadFoodById(anyLong())).thenReturn(food);
        Food storedFood = client.loadFoodById(food.getId());
        assertThat(storedFood).isEqualToComparingFieldByFieldRecursively(food);
    }
    
    @Test(expected=NotFoundException.class)
    public void whenLoadFoodByIdThenNotFoundException() {
        when(client.loadFoodById(anyLong())).thenThrow(NotFoundException.class);
        client.loadFoodById(food.getId());
    }
    
    @Test
    public void updateFood() {
        when(client.updateFood(anyLong(), any(Food.class))).thenReturn(food);
        Food updatedFood = client.updateFood(food.getId(), food);
        assertThat(updatedFood).isEqualToComparingFieldByFieldRecursively(food);
    }
    
    @Test
    public void deleteFood() {
        doNothing().when(client).deleteFood(anyLong());
        client.deleteFood(food.getId());
        verify(client, times(1)).deleteFood(food.getId());
    }

}
