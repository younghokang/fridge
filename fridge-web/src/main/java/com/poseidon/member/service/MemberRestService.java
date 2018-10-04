package com.poseidon.member.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.poseidon.member.model.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberRestService {
    private final RestTemplate memberRestTemplate;
    
    public Member loadByUsername(String username) {
        try {
            ResponseEntity<Member> response = memberRestTemplate.getForEntity("/members/{username}", Member.class, username);
            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch(HttpClientErrorException ex) {
            log.error("Response error: {} {}", ex.getStatusCode(), ex.getStatusText());
        }
        return null;
    }
    
    public Member register(Member member) {
        ResponseEntity<Member> response = memberRestTemplate.postForEntity("/members", member, Member.class);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        }
        return null;
    }

}
