package com.poseidon.fridge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JpaFoodRepository;

@RestController
@RequestMapping("/foods")
public class FoodController {
    
    @Autowired
    private JpaFoodRepository jpaFoodRepository;
    
    @RequestMapping(method=RequestMethod.GET)
    public List<Food> findAllFoods() {
        return jpaFoodRepository.findAll();
    }

}
