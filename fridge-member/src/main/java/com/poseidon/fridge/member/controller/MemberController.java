package com.poseidon.fridge.member.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;
import com.poseidon.fridge.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository repository;
    private final MemberResourceAssembler assembler;
    private final MemberService service;
    
    @GetMapping("/{username}")
    Resource<Member> findByUsername(@PathVariable String username) {
        Member member = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return assembler.toResource(member);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String usernameNotFoundHandler(UsernameNotFoundException ex) {
        return ex.getMessage();
    }
    
    @PostMapping
    ResponseEntity<?> registerNewMember(@RequestBody Member memberRequest) throws URISyntaxException {
        Member member = service.save(memberRequest);
        Resource<Member> resource = assembler.toResource(member);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    
    @PutMapping("/{id}")
    ResponseEntity<?> changeMemberPassword(@PathVariable final long id, @RequestBody final Member memberRequest) throws URISyntaxException {
        Member updatedMember = repository.findById(id)
                .map(member -> {
                    member.setPassword(memberRequest.getPassword());
                    return service.save(member);
                })
                .orElseGet(() -> {
                    memberRequest.setId(id);
                    return service.save(memberRequest);
                });
        Resource<Member> resource = assembler.toResource(updatedMember);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    
    @DeleteMapping("/{id}")
    ResponseEntity<?> withdrawal(@PathVariable final long id) {
        service.withdraw(id);
        return ResponseEntity.noContent().build();
    }
    
}
