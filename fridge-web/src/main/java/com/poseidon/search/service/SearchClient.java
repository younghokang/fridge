package com.poseidon.search.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="fridge-search")
public interface SearchClient {
    
    @GetMapping("/searchProductName")
    public String[] searchProductName(@RequestParam("query") String query);
    
    @PutMapping("/increaseScore")
    public void increaseScore(String query);

}
