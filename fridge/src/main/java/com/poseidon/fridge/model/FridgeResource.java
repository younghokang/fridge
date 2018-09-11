package com.poseidon.fridge.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FridgeResource extends ResourceSupport {
    @JsonProperty
    Integer id;
    @JsonProperty
    String nickname;
}
