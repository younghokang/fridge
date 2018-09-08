package com.poseidon.fridge;

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

import com.poseidon.fridge.command.FoodCommand;

@Controller
@RequestMapping("/web/foods")
public class WebFoodController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping
    public String foods(Model model) {
        return "food/foods";
    }
    
    @GetMapping("/add")
    public String registerFoodForm(@ModelAttribute("foodCommand") FoodCommand foodCommand) {
        return "food/registerFoodForm";
    }
    
    @PostMapping("/add")
    public String processRegistrationFood(@Valid FoodCommand foodCommand, Errors errors, RedirectAttributes ra) {
        if(errors.hasErrors()) {
            return "food/registerFoodForm";
        }
        
        ResponseEntity<FoodCommand> response = restTemplate.postForEntity("http://localhost:8081/foods", foodCommand, FoodCommand.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            ra.addFlashAttribute("registerFoodMessage", "식품을 저장했습니다.");
        }
        return "redirect:/web/foods";
    }

}
