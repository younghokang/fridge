package com.poseidon.fridge.service;

import java.util.List;

import com.poseidon.fridge.model.Food;

public interface FoodService {
    List<Food> findAll();
    Food save(Food food);
    boolean remove(Food food);
    Food findById(Long id);
}
