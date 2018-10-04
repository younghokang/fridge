package com.poseidon.fridge.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.fridge.service.FridgeRestService;
import com.poseidon.member.model.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/fridges")
@RequiredArgsConstructor
public class FridgeController {
    private final FridgeRestService service;
    
    @GetMapping("/me")
    public String myFridge(@AuthenticationPrincipal Member member, Model model) {
        model.addAttribute("fridge", service.loadMyFridge(member.getId()));
        return "fridges/fridge";
    }
    
}
