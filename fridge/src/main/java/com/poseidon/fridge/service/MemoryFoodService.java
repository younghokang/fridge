package com.poseidon.fridge.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.ImmutableList;
import com.poseidon.fridge.model.Food;

public class MemoryFoodService implements FoodService {
    private List<Food> foods = new ArrayList<>();
    private AtomicLong atomicLong = new AtomicLong();

    public List<Food> findAll() {
        List<Food> cloneFoods = new ArrayList<>();
        for(Food food : foods) {
            cloneFoods.add(food.clone());
        }
        return ImmutableList.<Food>builder().addAll(cloneFoods).build();
    }

    @Override
    public Food save(Food food) {
        if(foods.contains(food)) {
            int index = foods.indexOf(food);
            foods.set(index, food);
        } else {
            foods.add(food);
            food.setId(atomicLong.incrementAndGet());
        }
        return food;
    }

    @Override
    public boolean remove(Food food) {
        return foods.remove(food);
    }

    public Food findById(Long id) {
        for(Food food : foods) {
            if(food.getId() == id) {
                return food;
            }
        }
        return null;
    }

    @Override
    public void removeAll() {
        foods.clear();
    }

    @Override
    public void remove(Long id) {
        Iterator<Food> itr = foods.iterator();
        while(itr.hasNext()) {
            Food food = itr.next();
            if(food.getId() == id) {
                itr.remove();
                break;
            }
        }
    }

}
