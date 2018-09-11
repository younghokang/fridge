package com.poseidon.fridge.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.model.FridgeResource;
import com.poseidon.fridge.model.FridgeResourceAssembler;
import com.poseidon.fridge.service.FridgeService;

@RestController
@RequestMapping("/fridges")
public class FridgeController {
    
    @Autowired
    FridgeService fridgeService;
    
    FridgeResourceAssembler assembler = new FridgeResourceAssembler();
    
    @PostMapping
    public ResponseEntity<FridgeResource> create(@RequestBody final String nickname) {
        Fridge fridge = fridgeService.create(nickname);
        URI location = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")    
                .buildAndExpand(fridge.getId())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toResource(fridge));
    }

}
