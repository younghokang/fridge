package com.poseidon.fridge.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodResource extends ResourceSupport {
    @JsonProperty
    long id;
    @JsonProperty
    String name;
    @JsonProperty
    int quantity;
    @JsonProperty
    String expiryDate;
    
}
