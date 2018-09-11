package com.poseidon.food.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
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

import com.poseidon.food.model.Food;
import com.poseidon.food.model.FoodResource;
import com.poseidon.food.model.FoodResourceAssembler;
import com.poseidon.food.repository.JpaFoodRepository;
import com.poseidon.food.service.JpaFoodService;

@RestController
@RequestMapping("/foods")
public class FoodController {
    
    @Autowired
    private JpaFoodRepository jpaFoodRepository;
    
    @Autowired
    private JpaFoodService jpaFoodService;
    
    FoodResourceAssembler assembler = new FoodResourceAssembler();
    
    @GetMapping
    public ResponseEntity<Resources<FoodResource>> findAllFoods() {
        List<Food> foods = jpaFoodRepository.findAll();
        List<FoodResource> foodResources = assembler.toResources(foods);
        Link link = linkTo(methodOn(FoodController.class).findAllFoods()).withSelfRel();
        Resources<FoodResource> resources = new Resources<>(foodResources, link);
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FoodResource> findById(@PathVariable final long id) {
        Food food = jpaFoodRepository.findOne(id);
        if(food == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toResource(food));
    }
    
    @PostMapping
    public ResponseEntity<FoodResource> postFood(@RequestBody final Food food) {
        jpaFoodService.save(food);
        URI location = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")    
                .buildAndExpand(food.getId())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toResource(food));
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
