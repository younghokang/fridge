package com.poseidon.fridge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.poseidon.fridge.model.Fridge;

@RepositoryRestResource
public interface FridgeRepository extends JpaRepository<Fridge, Integer> {
    Optional<Fridge> findByUserId(@Param("userId") Long userId);
}
