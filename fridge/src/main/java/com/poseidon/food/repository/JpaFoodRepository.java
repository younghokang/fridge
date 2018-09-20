package com.poseidon.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.food.model.Food;

@Repository
public interface JpaFoodRepository extends JpaRepository<Food, Long> {
    List<Food> findAllByFridgeId(Integer fridgeId);
}
