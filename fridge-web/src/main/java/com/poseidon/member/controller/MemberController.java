package com.poseidon.member.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.poseidon.member.model.Member;
import com.poseidon.member.service.MemberRestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRestService service;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/signup")
    public String signupForm(Member member, Model model) {
        model.addAttribute("memeber", member);
        return "members/signup";
    }
    
    @PostMapping("/signup")
    public String signupProcessing(@ModelAttribute @Valid Member member, Errors errors) {
        if(errors.hasErrors()) {
            return "members/signup";
        }
        Member existsMember = service.loadByUsername(member.getUsername());
        if(existsMember != null) {
            errors.rejectValue("username", "field.exists.member.username");
            return "members/signup";
        }
        
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        service.register(member);
        return "redirect:/signin";
    }
    
    @GetMapping("/signin")
    public String signinForm() {
        return "members/signin";
    }
    
}
