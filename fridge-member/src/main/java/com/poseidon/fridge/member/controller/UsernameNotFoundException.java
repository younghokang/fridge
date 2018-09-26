package com.poseidon.fridge.member.controller;

public class UsernameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException(String username) {
        super("could not found username by " + username);
    }

}
