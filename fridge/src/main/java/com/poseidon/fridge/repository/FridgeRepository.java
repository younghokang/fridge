package com.poseidon.fridge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.fridge.model.Fridge;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Integer> {
    Optional<Fridge> findByUserId(Long userId);
    Optional<Fridge> findById(Integer id);
}
