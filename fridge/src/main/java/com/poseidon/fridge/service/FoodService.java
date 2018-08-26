package com.poseidon.fridge.service;

import org.springframework.transaction.annotation.Transactional;

import com.poseidon.fridge.model.Food;

public interface FoodService {
    @Transactional
    Food save(Food food);
    
    @Transactional
    boolean remove(Food food);
    
    @Transactional
    void removeAll();
}
