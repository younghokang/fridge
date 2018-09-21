package com.poseidon.fridge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.fridge.service.FridgeRestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/fridges")
@RequiredArgsConstructor
public class FridgeController {
    
    private final FridgeRestService service;
    
    @GetMapping("/me")
    public String myFridge(Model model) {
        model.addAttribute("fridge", service.loadMyFridge());
        return "fridges/fridge";
    }
    
}
