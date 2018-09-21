package com.poseidon.fridge.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.poseidon.fridge.model.Fridge;

@Component
public class FridgeResourceAssembler implements ResourceAssembler<Fridge, Resource<Fridge>> {

    @Override
    public Resource<Fridge> toResource(Fridge fridge) {
        return new Resource<>(fridge, 
                linkTo(methodOn(FridgeController.class).loadFridgeById(fridge.getId())).withSelfRel(),
                linkTo(methodOn(FridgeController.class).findAllFridges()).withRel("fridges"));
    }

}
