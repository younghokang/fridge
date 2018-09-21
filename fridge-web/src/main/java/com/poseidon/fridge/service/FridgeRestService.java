package com.poseidon.fridge.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.poseidon.fridge.command.FridgeCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FridgeRestService {
    private final RestTemplate restTemplate;
    
    private static final String DEFAULT_NICKNAME = "myFridge";
    private static final long USER_ID = 1004L;
    
    public FridgeCommand loadMyFridge() {
        FridgeCommand fridgeCommand = loadByUserId(USER_ID);
        if(fridgeCommand == null) {
            return generate(DEFAULT_NICKNAME, USER_ID);
        }
        return fridgeCommand;
    }
    
    FridgeCommand loadByUserId(long userId) {
        try {
            ResponseEntity<FridgeCommand> response = restTemplate.getForEntity("/fridges/me/" + USER_ID, FridgeCommand.class);
            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch(HttpClientErrorException ex) {
            log.error("Response error: {} {}", ex.getStatusCode(), ex.getStatusText());
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
