package com.poseidon.fridge.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.model.FridgeResource;
import com.poseidon.fridge.model.FridgeResourceAssembler;
import com.poseidon.fridge.repository.JpaFridgeRepository;
import com.poseidon.fridge.service.FridgeService;

@RestController
@RequestMapping("/fridges")
public class FridgeController {
    
    @Autowired
    private FridgeService fridgeService;
    
    @Autowired
    private JpaFridgeRepository jpaFridgeRepository;
    
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
    
    @GetMapping("/{id}")
    public ResponseEntity<FridgeResource> loadFridgeById(@PathVariable final int id) {
        Fridge fridge = jpaFridgeRepository.findOne(id);
        if(fridge == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(assembler.toResource(fridge));
    }
    
    @GetMapping
    public ResponseEntity<Resources<FridgeResource>> findAllFridges() {
        List<Fridge> fridges = jpaFridgeRepository.findAll();
        List<FridgeResource> fridgeResources = assembler.toResources(fridges);
        Link link = linkTo(methodOn(FridgeController.class).findAllFridges()).withSelfRel();
        Resources<FridgeResource> resources = new Resources<>(fridgeResources, link);
        return ResponseEntity.ok(resources);
    }

}
