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
import com.poseidon.food.repository.JpaFoodRepository;
import com.poseidon.food.service.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
@Slf4j
public class FoodController {
    private final JpaFoodRepository repository;
    private final FoodService service;
    private FoodResourceAssembler assembler = new FoodResourceAssembler();
    
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
        Food food = repository.findOne(id);
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
            @RequestBody final FoodRequest foodRequest) {
        log.info("foodRequest: " + foodRequest.toString());
        if(repository.findOne(id) != null) {
            service.save(foodRequest.toFood());
        }
        return ResponseEntity.noContent().build();
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
