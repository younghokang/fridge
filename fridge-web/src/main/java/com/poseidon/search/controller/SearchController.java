package com.poseidon.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.search.service.SearchClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchClient client;
    
    @GetMapping("/searchProductName")
    public String[] searchProductName(String query) {
        return client.searchProductName(query);
    }

}
