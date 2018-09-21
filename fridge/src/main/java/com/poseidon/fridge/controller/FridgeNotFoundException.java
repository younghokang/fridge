package com.poseidon.fridge.controller;

public class FridgeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FridgeNotFoundException(Integer id) {
        super("could not found fridge #" + id);
    }
    
    public FridgeNotFoundException(Long userId) {
        super("could not found fridge by userId #" + userId);
    }
}
