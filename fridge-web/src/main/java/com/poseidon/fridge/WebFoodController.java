package com.poseidon.fridge;

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
import org.springframework.web.bind.annotation.PutMapping;
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
        ResponseEntity<Resources<FoodCommand>> response = restTemplate.exchange("http://localhost:8081/foods", 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<Resources<FoodCommand>>() {}, 
                Collections.emptyMap());
        if(response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("foodList", response.getBody().getContent());
        }
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
    
    @GetMapping("/{id}")
    public String updateFoodForm(@PathVariable long id, Model model) {
        ResponseEntity<FoodCommand> response = restTemplate.getForEntity("http://localhost:8081/foods/{id}", FoodCommand.class, id);
        if(response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("foodCommand", response.getBody());
        }
        return "food/updateFoodForm";
    }
    
    @PutMapping("/{id}")
    public String processUpdateFood(@PathVariable long id, @Valid FoodCommand foodCommand, Errors errors, RedirectAttributes ra) {
        if(errors.hasErrors()) {
            return "food/updateFoodForm";
        }
        
        restTemplate.put("http://localhost:8081/foods/{id}", foodCommand, id);
        ra.addFlashAttribute("registerFoodMessage", "식품을 저장했습니다.");
        return "redirect:/web/foods";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable long id, RedirectAttributes ra) {
        restTemplate.delete("http://localhost:8081/foods/{id}", id);
        ra.addFlashAttribute("registerFoodMessage", "식품을 삭제했습니다.");
        return "redirect:/web/foods";
    }

}
