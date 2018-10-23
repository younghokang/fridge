package com.poseidon.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.poseidon.food.model.Food;

@RepositoryRestResource
public interface FoodRepository extends JpaRepository<Food, Long> {
    
}
