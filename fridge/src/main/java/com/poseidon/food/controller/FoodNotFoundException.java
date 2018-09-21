package com.poseidon.food.controller;

public class FoodNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FoodNotFoundException(long id) {
        super("could not found Food #" + id);
    }
    
}
