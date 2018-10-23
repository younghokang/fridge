package com.poseidon.fridge.controller;

import org.springframework.hateoas.Resources;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.food.command.Food;
import com.poseidon.fridge.command.Fridge;
import com.poseidon.fridge.service.FridgeClient;
import com.poseidon.fridge.service.NotFoundException;
import com.poseidon.member.model.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/fridges")
@RequiredArgsConstructor
@Slf4j
public class FridgeController {
    private final FridgeClient client;
    
    private static final String DEFAULT_NICKNAME = "myFridge";
    
    @GetMapping("/me")
    public String myFridge(@AuthenticationPrincipal Member member, Model model) {
        Fridge fridge = null;
        try {
            fridge = client.loadByUserId(member.getId());
        } catch(NotFoundException ex) {
            log.info(ex.getMessage());
            fridge = client.generate(Fridge.builder()
                    .nickname(DEFAULT_NICKNAME)
                    .userId(member.getId()).build());
        }
        
        Resources<Food> resources = client.loadFoodsByFridgeId(fridge.getId());
        fridge.setFoods(resources.getContent());
        model.addAttribute("fridge", fridge);
        return "fridges/fridge";
    }
    
}
