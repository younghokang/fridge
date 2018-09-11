package com.poseidon.food.model;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.poseidon.food.controller.FoodController;

public class FoodResourceAssembler extends ResourceAssemblerSupport<Food, FoodResource> {

    public FoodResourceAssembler() {
        super(FoodController.class, FoodResource.class);
    }

    @Override
    public FoodResource toResource(Food food) {
        FoodResource foodResource = new FoodResource();
        foodResource.id = food.getId();
        foodResource.name = food.getName();
        foodResource.quantity = food.getQuantity();
        foodResource.expiryDate = food.getExpiryDate();
        foodResource.add(linkTo(FoodController.class).slash(foodResource.id).withSelfRel());
        return foodResource;
    }
    
}
