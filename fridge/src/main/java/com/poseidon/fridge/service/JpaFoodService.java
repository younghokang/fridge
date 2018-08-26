package com.poseidon.fridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JpaFoodRepository;

@Service
public class JpaFoodService implements FoodService {
    
    @Autowired
    private JpaFoodRepository jpaFoodRepository;

    @Override
    public Food save(Food food) {
        return jpaFoodRepository.save(food);
    }

    @Override
    public boolean remove(Food food) {
        jpaFoodRepository.delete(food);
        return true;
    }

    @Override
    public void removeAll() {
        jpaFoodRepository.deleteAll();
    }

}
