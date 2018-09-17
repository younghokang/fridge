package com.poseidon.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.fridge.model.Fridge;

@Repository
public interface JpaFridgeRepository extends JpaRepository<Fridge, Integer> {
    Fridge findByUserId(Long userId);

}
