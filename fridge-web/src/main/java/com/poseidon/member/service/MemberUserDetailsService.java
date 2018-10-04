package com.poseidon.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.poseidon.member.model.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRestService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = service.loadByUsername(username);
        if(member == null) {
            throw new UsernameNotFoundException("could not found user by username: " + username);
        }
        return member;
    }

}
