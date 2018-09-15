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
        FoodResource resource = new FoodResource();
        resource.id = food.getId();
        resource.name = food.getName();
        resource.quantity = food.getQuantity();
        resource.expiryDate = food.getExpiryDate();
        resource.fridge = food.getFridge();
        resource.add(linkTo(FoodController.class).slash(resource.id).withSelfRel());
        return resource;
    }
    
}
