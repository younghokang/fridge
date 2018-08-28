package com.poseidon.fridge.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JpaFoodRepository;
import com.poseidon.fridge.service.JpaFoodService;

@RestController
@RequestMapping("/foods")
public class FoodController {
    
    @Autowired
    private JpaFoodRepository jpaFoodRepository;
    
    @Autowired
    private JpaFoodService jpaFoodService;
    
    @GetMapping
    public List<Food> findAllFoods() {
        return jpaFoodRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Food> findById(@PathVariable final long id) {
        Food food = jpaFoodRepository.findOne(id);
        if(food == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(food);
    }
    
    @PostMapping
    public ResponseEntity<Food> postFood(@RequestBody final Food food) {
        jpaFoodService.save(food);
        URI location = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")    
                .buildAndExpand(food.getId())
                .toUri();
        return ResponseEntity.created(location).body(food);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable final long id, @RequestBody final Food food) {
        if(jpaFoodRepository.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        }
        jpaFoodService.save(food);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable final long id) {
        if(jpaFoodRepository.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        }
        jpaFoodService.remove(id);
        return ResponseEntity.noContent().build();
    }
    

}
