package com.poseidon.fridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.repository.JpaFridgeRepository;

@Service
public class FridgeServiceImpl implements FridgeService {
    
    @Autowired
    JpaFridgeRepository jpaFridgeRepository;
    
    @Override
    public Fridge create(String nickname) {
        Fridge fridge = new Fridge(nickname);
        return jpaFridgeRepository.save(fridge);
    }

}
