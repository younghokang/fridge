package com.poseidon.food.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.poseidon.food.model.Food;

@Component
public class FoodResourceAssembler implements ResourceAssembler<Food, Resource<Food>> {

    @Override
    public Resource<Food> toResource(Food food) {
        return new Resource<>(food, 
                linkTo(methodOn(FoodController.class).findById(food.getId())).withSelfRel(),
                linkTo(methodOn(FoodController.class).findAllFoods()).withRel("foods"));
    }
    
}
