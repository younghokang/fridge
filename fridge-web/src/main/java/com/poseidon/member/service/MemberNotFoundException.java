package com.poseidon.member.service;

public class MemberNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MemberNotFoundException(int status, String reason) {
        super(String.format("status %s reason %s", status, reason));
    }
    
}
