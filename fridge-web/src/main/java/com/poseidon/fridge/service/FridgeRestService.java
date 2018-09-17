package com.poseidon.fridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.poseidon.fridge.command.FridgeCommand;

@Service
public class FridgeRestService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String DEFAULT_NICKNAME = "myFridge";
    private static final long USER_ID = 1004L;
    
    public FridgeCommand loadMyFridge() {
        FridgeCommand fridge = loadByUserId(USER_ID);
        if(fridge == null) {
            return generate(DEFAULT_NICKNAME, USER_ID);
        }
        return fridge;
    }
    
    FridgeCommand loadByUserId(long userId) {
        ResponseEntity<FridgeCommand> response = restTemplate.getForEntity("/fridges/me/" + USER_ID, FridgeCommand.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }
    
    FridgeCommand generate(String nickname, long userId) {
        FridgeCommand fridgeCommand = new FridgeCommand();
        fridgeCommand.setNickname(nickname);
        fridgeCommand.setUserId(userId);
        
        ResponseEntity<FridgeCommand> response = restTemplate.postForEntity("/fridges", fridgeCommand, FridgeCommand.class);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        }
        return null;
    }

}
