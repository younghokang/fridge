package com.poseidon.fridge.service;

import org.springframework.transaction.annotation.Transactional;

import com.poseidon.fridge.model.Fridge;

@Transactional
public interface FridgeService {
    Fridge create(String nickname);
}
