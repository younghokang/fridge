package com.poseidon.fridge.search.productname.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.fridge.search.productname.ProductNameTrie;
import com.poseidon.fridge.search.productname.model.ProductName;
import com.poseidon.fridge.search.productname.repository.ProductNameRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductNameController {
    private final ProductNameTrie trie;
    private final ProductNameRepository productNameRepository;
    
    @GetMapping("/searchProductName")
    public String[] searchProductName(String query) {
        log.info("search query: {}", query);
        return trie.search(query);
    }
    
    @PutMapping("/increaseScore")
    public ResponseEntity<Void> increaseScore(@RequestBody String query) {
        log.info("increase query: {}", query);
        Optional.ofNullable(trie.increaseScore(query))
            .ifPresent(score -> {
                productNameRepository.save(ProductName.builder()
                        .name(query)
                        .score(score + 1)
                        .build());
            });
        return ResponseEntity.noContent().build();
    }

}
