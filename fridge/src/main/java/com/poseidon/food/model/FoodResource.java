package com.poseidon.food.model;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poseidon.fridge.model.Fridge;

public class FoodResource extends ResourceSupport {
    @JsonProperty
    Long id;
    @JsonProperty
    String name;
    @JsonProperty
    Integer quantity;
    @JsonProperty
    Date expiryDate;
    @JsonProperty
    Fridge fridge;
}
