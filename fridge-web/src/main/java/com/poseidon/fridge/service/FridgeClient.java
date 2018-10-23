package com.poseidon.fridge.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poseidon.config.FeignConfig;
import com.poseidon.food.command.Food;
import com.poseidon.fridge.command.Fridge;

@FeignClient(name="fridge-service", configuration=FeignConfig.class)
public interface FridgeClient {
    
    @GetMapping("/fridges/search/findByUserId")
    Fridge loadByUserId(@RequestParam("userId") long userId) throws NotFoundException;
    
    @GetMapping("/fridges/{id}/foods")
    Resources<Food> loadFoodsByFridgeId(@PathVariable("id") int id);
    
    @PostMapping("/fridges")
    Fridge generate(Fridge fridge);
    
    @DeleteMapping("/fridges")
    void deleteAll();
    
    @PostMapping("/foods")
    Food createFood(Food food);
    
    @GetMapping("/foods/{id}")
    Food loadFoodById(@PathVariable("id") long id) throws NotFoundException;
    
    @PutMapping("/foods/{id}")
    Food updateFood(@PathVariable("id") long id, Food food);
    
    @DeleteMapping("/foods/{id}")
    void deleteFood(@PathVariable("id") long id);

}
