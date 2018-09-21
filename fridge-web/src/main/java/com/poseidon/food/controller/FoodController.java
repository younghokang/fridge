package com.poseidon.food.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.food.command.FoodCommand;
import com.poseidon.food.service.FoodRestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/fridges/{fridgeId}/foods")
@SessionAttributes("foodCommand")
@RequiredArgsConstructor
public class FoodController {
    private final FoodRestService service;
    
    @GetMapping("/add")
    public String registerFoodForm(FoodCommand foodCommand, Model model) {
        model.addAttribute("foodCommand", foodCommand);
        return "foods/registerFoodForm";
    }
    
    @PostMapping("/add")
    public String processRegistrationFood(@ModelAttribute @Valid FoodCommand foodCommand, 
            Errors errors, 
            RedirectAttributes ra, 
            SessionStatus sessionStatus) {
        if(errors.hasErrors()) {
            return "foods/registerFoodForm";
        }
        
        if(service.create(foodCommand) != null) {
            ra.addFlashAttribute("message", "식품을 저장했습니다.");
            sessionStatus.setComplete();
        }
        return "redirect:/fridges/me";
    }
    
    @GetMapping("/{id}")
    public String updateFoodForm(@PathVariable("fridgeId") Integer fridgeId, 
            @PathVariable long id, Model model) {
        FoodCommand foodCommand = service.loadById(id);
        foodCommand.setFridgeId(fridgeId);
        model.addAttribute("foodCommand", foodCommand);
        return "foods/updateFoodForm";
    }
    
    @PutMapping("/{id}")
    public String processUpdateFood(@PathVariable long id, 
            @ModelAttribute @Valid FoodCommand foodCommand, 
            Errors errors, 
            RedirectAttributes ra,
            SessionStatus sessionStatus) {
        if(errors.hasErrors()) {
            return "foods/updateFoodForm";
        }
        
        service.update(foodCommand, id);
        ra.addFlashAttribute("message", "식품을 저장했습니다.");
        sessionStatus.setComplete();
        return "redirect:/fridges/me";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "식품을 삭제했습니다.");
        return "redirect:/fridges/me";
    }

}
