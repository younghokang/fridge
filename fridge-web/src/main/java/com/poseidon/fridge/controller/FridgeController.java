package com.poseidon.fridge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.fridge.service.FridgeRestService;

@Controller
@RequestMapping("/fridges")
public class FridgeController {
    
    @Autowired
    private FridgeRestService fridgeRestService;
    
    @GetMapping("/me")
    public String myFridge(Model model) {
        model.addAttribute("fridge", fridgeRestService.loadMyFridge());
        return "fridges/fridge";
    }
    
}
