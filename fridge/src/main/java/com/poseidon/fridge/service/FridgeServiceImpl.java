package com.poseidon.fridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.repository.FridgeRepository;

@Service
public class FridgeServiceImpl implements FridgeService {
    
    @Autowired
    FridgeRepository repository;
    
    @Override
    public Fridge create(String nickname, long userId) {
        Fridge fridge = Fridge.builder()
                .nickname(nickname)
                .userId(userId)
                .build();
        return repository.save(fridge);
    }

    @Override
    public Fridge save(Fridge fridge) {
        return repository.save(fridge);
    }

    @Override
    public void remove(int id) {
        repository.delete(id);
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
    }

}
