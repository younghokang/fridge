package com.poseidon.search.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poseidon.search.command.Category;

@FeignClient(name="fridge-search")
public interface SearchClient {
    
    @GetMapping("/searchProductName")
    public String[] searchProductName(@RequestParam("query") String query);
    
    @PutMapping("/increaseScore")
    public void increaseScore(String query);
    
    @GetMapping("/classifyCategories")
    public Category classifyCategories(@RequestParam("query") String query);
    
    @GetMapping("/categories/{id}")
    public Category findCategoryById(@PathVariable("id") long id);
    
    @GetMapping("/categoryNames")
    public List<Category> categoryNames();

}
