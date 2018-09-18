package com.poseidon.food.model;

import java.time.LocalDate;

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
    LocalDate expiryDate;
    @JsonProperty
    Fridge fridge;
}
