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
    private final RestTemplate fridgeServiceRestTemplate;
    
    private static final String DEFAULT_NICKNAME = "myFridge";
    
    public FridgeCommand loadMyFridge(long id) {
        FridgeCommand fridgeCommand = loadByUserId(id);
        if(fridgeCommand == null) {
            return generate(DEFAULT_NICKNAME, id);
        }
        return fridgeCommand;
    }
    
    FridgeCommand loadByUserId(long id) {
        try {
            ResponseEntity<FridgeCommand> response = fridgeServiceRestTemplate.getForEntity("/fridges/me/" + id, FridgeCommand.class);
            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch(HttpClientErrorException ex) {
            log.error("Response error: {} {}", ex.getStatusCode(), ex.getStatusText());
        }
        return null;
    }
    
    FridgeCommand generate(String nickname, long id) {
        FridgeCommand fridgeCommand = new FridgeCommand();
        fridgeCommand.setNickname(nickname);
        fridgeCommand.setUserId(id);
        
        ResponseEntity<FridgeCommand> response = fridgeServiceRestTemplate.postForEntity("/fridges", fridgeCommand, FridgeCommand.class);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        }
        return null;
    }

}
