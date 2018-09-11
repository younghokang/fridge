package com.poseidon.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.food.model.Food;

@Repository
public interface JpaFoodRepository extends JpaRepository<Food, Long> {

}
