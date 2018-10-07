package com.poseidon.fridge.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

@FeignClient(name="fridge-service")
public interface FridgeClient {
    
    @GetMapping("/fridges/me/{userId}")
    FridgeCommand loadByUserId(@PathVariable("userId") long userId) throws NotFoundException;
    
    @PostMapping("/fridges")
    FridgeCommand generate(FridgeCommand fridgeCommand);
    
    @DeleteMapping("/fridges")
    void deleteAll();
    
    @PostMapping("/foods")
    FoodCommand createFood(FoodCommand foodCommand);
    
    @GetMapping("/foods/{id}")
    FoodCommand loadFoodById(@PathVariable("id") long id) throws NotFoundException;
    
    @PutMapping("/foods/{id}")
    FoodCommand updateFood(@PathVariable("id") long id, FoodCommand foodCommand);
    
    @DeleteMapping("/foods/{id}")
    void deleteFood(@PathVariable("id") long id);

}
