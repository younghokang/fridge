package com.poseidon.fridge.member.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
    
    public Member save(Member member) {
        return repository.save(member);
    }

    public void withdraw(long id) {
        repository.deleteById(id);
    }

}
