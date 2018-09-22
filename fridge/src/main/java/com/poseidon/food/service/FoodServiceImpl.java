package com.poseidon.food.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.food.model.Food;
import com.poseidon.food.repository.FoodRepository;

@Service
public class FoodServiceImpl implements FoodService {
    
    @Autowired
    private FoodRepository repository;
    
    @Override
    public Food save(Food food) {
        return repository.save(food);
    }

    @Override
    public boolean remove(Food food) {
        repository.delete(food);
        return true;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }

}
