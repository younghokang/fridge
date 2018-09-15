package com.poseidon.food.model;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poseidon.fridge.model.Fridge;

public class FoodResource extends ResourceSupport {
    @JsonProperty
    long id;
    @JsonProperty
    String name;
    @JsonProperty
    int quantity;
    @JsonProperty
    Date expiryDate;
    @JsonProperty
    Fridge fridge; 
}
