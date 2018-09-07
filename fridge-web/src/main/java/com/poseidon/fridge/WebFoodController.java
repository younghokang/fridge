package com.poseidon.fridge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/foods")
public class WebFoodController {
    
    @GetMapping
    public String foods() {
        return "food/foods";
    }

}
