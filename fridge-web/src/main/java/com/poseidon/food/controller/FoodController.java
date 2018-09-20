package com.poseidon.food.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/foods")
@SessionAttributes("foodCommand")
public class FoodController {
    
    @Autowired
    private FoodRestService foodRestService;
    
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
        
        if(foodRestService.create(foodCommand) != null) {
            ra.addFlashAttribute("message", "식품을 저장했습니다.");
            sessionStatus.setComplete();
        }
        return "redirect:/fridges/me";
    }
    
    @GetMapping("/{id}")
    public String updateFoodForm(@PathVariable long id, Model model) {
        model.addAttribute("foodCommand", foodRestService.loadById(id));
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
        
        foodRestService.update(foodCommand, id);
        ra.addFlashAttribute("message", "식품을 저장했습니다.");
        sessionStatus.setComplete();
        return "redirect:/fridges/me";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable long id, RedirectAttributes ra) {
        foodRestService.delete(id);
        ra.addFlashAttribute("message", "식품을 삭제했습니다.");
        return "redirect:/fridges/me";
    }

}
