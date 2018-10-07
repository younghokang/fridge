package com.poseidon.fridge.service;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(int status, String reason) {
        super(String.format("status %s reason %s", status, reason));
    }

}
