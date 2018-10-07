package com.poseidon.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return client.loadByUsername(username).toMember();
        } catch(MemberNotFoundException ex) {
            throw new UsernameNotFoundException("could not found user by username: " + username);
        }
    }

}
