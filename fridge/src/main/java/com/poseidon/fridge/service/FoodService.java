package com.poseidon.fridge.service;

import org.springframework.transaction.annotation.Transactional;

import com.poseidon.fridge.model.Food;

@Transactional
public interface FoodService {
    Food save(Food food);
    boolean remove(Food food);
    void remove(Long id);
    void removeAll();
}
