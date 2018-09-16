package com.poseidon.food.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.food.model.Food;
import com.poseidon.food.repository.JpaFoodRepository;

@Service
public class FoodServiceImpl implements FoodService {
    
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

    @Override
    public void remove(Long id) {
        jpaFoodRepository.delete(id);
    }

}
