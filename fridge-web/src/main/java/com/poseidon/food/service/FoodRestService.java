package com.poseidon.food.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.poseidon.food.command.FoodCommand;

@Service
public class FoodRestService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public FoodCommand create(FoodCommand foodCommand) {
        ResponseEntity<FoodCommand> response = restTemplate.postForEntity("/foods", foodCommand, FoodCommand.class);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        }
        return null;
    }
    
    public FoodCommand loadById(long id) {
        ResponseEntity<FoodCommand> response = restTemplate.getForEntity("/foods/{id}", FoodCommand.class, id);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }
    
    public void update(FoodCommand foodCommand, long id) {
        restTemplate.put("/foods/{id}", foodCommand, id);
    }
    
    public void delete(long id) {
        restTemplate.delete("/foods/{id}", id);
    }

}
