package com.poseidon.food.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
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

import com.poseidon.food.model.Food;
import com.poseidon.food.model.FoodRequest;
import com.poseidon.food.repository.FoodRepository;
import com.poseidon.food.service.FoodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FoodRepository repository;
    private final FoodService service;
    private final FoodResourceAssembler assembler;
    
    @GetMapping
    Resources<Resource<Food>> findAllFoods() {
        List<Resource<Food>> foods = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        
        return new Resources<>(foods, 
                linkTo(methodOn(FoodController.class).findAllFoods()).withSelfRel());
    }
    
    @GetMapping("/{id}")
    Resource<Food> findById(@PathVariable final long id) {
        Food food = repository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
        return assembler.toResource(food);
    }
    
    @PostMapping
    ResponseEntity<?> postFood(@RequestBody final FoodRequest foodRequest) throws URISyntaxException {
        Food food = service.save(foodRequest.toFood());
        Resource<Food> resource = assembler.toResource(food);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    
    @PutMapping("/{id}")
    ResponseEntity<?> updateFood(@PathVariable final long id, 
            @RequestBody final FoodRequest foodRequest) throws URISyntaxException {
        Food updatedFood = repository.findById(id)
                .map(food -> {
                    food.setName(foodRequest.getName());
                    food.setQuantity(foodRequest.getQuantity());
                    food.setExpiryDate(foodRequest.getExpiryDate());
                    food.setFridge(foodRequest.getFridge());
                    return repository.save(food);
                })
                .orElseGet(() -> {
                    foodRequest.setId(id);
                    return repository.save(foodRequest.toFood());
                });
        Resource<Food> resource = assembler.toResource(updatedFood);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFood(@PathVariable final long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping
    ResponseEntity<?> deleteAllFood() {
        service.removeAll();
        return ResponseEntity.noContent().build();
    }

}
