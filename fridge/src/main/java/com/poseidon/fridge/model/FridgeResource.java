package com.poseidon.fridge.model;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poseidon.food.model.Food;

public class FridgeResource extends ResourceSupport {
    @JsonProperty
    Integer id;
    @JsonProperty
    String nickname;
    @JsonProperty
    List<Food> foods;
}
