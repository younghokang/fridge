package com.poseidon.fridge.model;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.poseidon.fridge.controller.FridgeController;

public class FridgeResourceAssembler extends ResourceAssemblerSupport<Fridge, FridgeResource> {

    public FridgeResourceAssembler() {
        super(FridgeController.class, FridgeResource.class);
    }

    @Override
    public FridgeResource toResource(Fridge fridge) {
        FridgeResource resource = new FridgeResource();
        resource.id = fridge.getId();
        resource.nickname = fridge.getNickname();
        resource.foods = fridge.getFoods();
        resource.add(linkTo(FridgeController.class).slash(resource.id).withSelfRel());
        return resource;
    }

}
