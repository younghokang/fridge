package com.poseidon.fridge.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        ResponseEntity<Resources<FridgeCommand>> response = restTemplate.exchange("/fridges", 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<Resources<FridgeCommand>>() {}, 
                Collections.emptyMap());
        if(response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("fridgeList", response.getBody().getContent());
        }
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
        
        String nickname = fridgeCommand.getNickname();
        ResponseEntity<FridgeCommand> response = restTemplate.postForEntity("/fridges", nickname, FridgeCommand.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            ra.addFlashAttribute("registerMessage", fridgeCommand.getNickname() + "을 생성했습니다.");
        }
        return "redirect:/fridges";
    }
    
    @GetMapping("/{id}")
    public String updateFridgeForm(@PathVariable int id, Model model) {
        ResponseEntity<FridgeCommand> response = restTemplate.getForEntity("/fridges/{id}", FridgeCommand.class, id);
        if(response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("fridgeCommand", response.getBody());
        }
        return "fridges/updateFridgeForm";
    }
    
}
