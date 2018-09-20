package com.poseidon.fridge.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.model.FridgeRequest;
import com.poseidon.fridge.repository.JpaFridgeRepository;
import com.poseidon.fridge.service.FridgeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fridges")
public class FridgeController {
    
    private final FridgeService service;
    private final JpaFridgeRepository repository;
    private FridgeResourceAssembler assembler = new FridgeResourceAssembler();
    
    @PostMapping
    ResponseEntity<?> create(@RequestBody final FridgeRequest fridgeRequest) throws URISyntaxException {
        Fridge fridge = service.create(fridgeRequest.getNickname(), fridgeRequest.getUserId());
        Resource<Fridge> resource = assembler.toResource(fridge);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    
    @GetMapping("/{id}")
    Resource<Fridge> loadFridgeById(@PathVariable final int id) {
        Fridge fridge = repository.findOne(id);
        return assembler.toResource(fridge);
    }
    
    @GetMapping
    Resources<Resource<Fridge>> findAllFridges() {
        List<Resource<Fridge>> fridges = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        
        return new Resources<>(fridges, 
                linkTo(methodOn(FridgeController.class).findAllFridges()).withSelfRel());
    }
    
    @PutMapping("/{id}")
    ResponseEntity<?> updateFridge(@PathVariable final int id, @RequestBody final FridgeRequest fridgeRequest) {
        if(repository.findOne(id) != null) {
            service.save(fridgeRequest.toFridge());
        }
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFridgeById(@PathVariable final int id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping
    ResponseEntity<?> deleteAllFridge() {
        service.removeAll();
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/me/{userId}")
    Resource<Fridge> loadMyFridge(@PathVariable final long userId) {
        Fridge fridge = repository.findByUserId(userId);
        return assembler.toResource(fridge);
    }
    
}
