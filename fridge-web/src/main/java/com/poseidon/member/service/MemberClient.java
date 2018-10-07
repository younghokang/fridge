package com.poseidon.member.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.member.model.MemberRequest;

@FeignClient(name="fridge-member")
@RequestMapping("/members")
public interface MemberClient {
    
    @GetMapping("/{username}")
    MemberRequest loadByUsername(@PathVariable("username") String username) throws MemberNotFoundException;
    
    @PostMapping
    MemberRequest register(MemberRequest memberRequest);
    
    @PutMapping("/{id}")
    MemberRequest changePassword(@PathVariable("id") long id, MemberRequest memberRequest);
    
    @DeleteMapping("/{id}")
    void withdraw(@PathVariable("id") long id);
    
}
