package com.poseidon.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.fridge.model.Food;

@Repository
public interface JpaFoodRepository extends JpaRepository<Food, Long> {

}
