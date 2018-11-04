package com.poseidon.fridge.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.fridge.category.CategoryClassifier;
import com.poseidon.fridge.category.model.Category;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryClassifier classifier;
    
    @GetMapping("/classifyCategories")
    public Category classifyCategories(String query) {
        return classifier.process(query);
    }
    
    @GetMapping("/categoryNames")
    public List<Category> categoryNames() {
        return classifier.getCategories();
    }

}
