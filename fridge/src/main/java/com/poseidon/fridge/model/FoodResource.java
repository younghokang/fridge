package com.poseidon.fridge.model;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodResource extends ResourceSupport {
    @JsonProperty
    public long id;
    @JsonProperty
    public String name;
    @JsonProperty
    public int quantity;
    @JsonProperty
    public Date expiryDate;
    
}
