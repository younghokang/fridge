package com.poseidon.fridge.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.fridge.command.FridgeCommand;

@Controller
@RequestMapping("/fridges")
public class FridgeController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping
    public String fridges(Model model) {
        return "fridges/fridges";
    }
    
    @GetMapping("/add")
    public String registerFridgeForm(@ModelAttribute("fridgeCommand") FridgeCommand fridgeCommand) {
        return "fridges/registerFridgeForm";
    }
    
    @PostMapping("/add")
    public String processRegistrationFridge(@Valid FridgeCommand fridgeCommand, Errors errors, RedirectAttributes ra) {
        if(errors.hasErrors()) {
            return "fridges/registerFridgeForm";
        }
        
        ResponseEntity<FridgeCommand> response = restTemplate.postForEntity("/fridges", fridgeCommand, FridgeCommand.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            ra.addFlashAttribute("registerMessage", fridgeCommand.getNickname() + "을 생성했습니다.");
        }
        return "redirect:/fridges";
    }

}
