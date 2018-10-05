package com.poseidon.member.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.poseidon.member.model.Member;
import com.poseidon.member.model.MemberRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberRestService {
    private final RestTemplate memberRestTemplate;
    
    public Member loadByUsername(String username) {
        try {
            ResponseEntity<MemberRequest> response = memberRestTemplate.getForEntity("/members/{username}", MemberRequest.class, username);
            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody().toMember();
            }
        } catch(HttpClientErrorException ex) {
            log.error("Response error: {} {}", ex.getStatusCode(), ex.getStatusText());
        }
        return null;
    }
    
    public Member register(MemberRequest memberRequest) {
        ResponseEntity<MemberRequest> response = memberRestTemplate.postForEntity("/members", memberRequest, MemberRequest.class);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody().toMember();
        }
        return null;
    }

}
