package com.poseidon.fridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.command.FridgeCommand;

@RunWith(SpringRunner.class)
@RestClientTest(FridgeRestService.class)
public class FridgeRestServiceTests {
    
    @Autowired
    FridgeRestService service;
    
    @Autowired
    private MockRestServiceServer server;
    
    @Autowired
    private ObjectMapper mapper;
    
    private FridgeCommand fridgeCommand;
    
    @Before
    public void setUp() {
        fridgeCommand = new FridgeCommand();
        fridgeCommand.setId(1);
        fridgeCommand.setNickname("myFridge");
        fridgeCommand.setUserId(1004L);
    }
    
    @Test
    public void loadByUserId() throws IOException {
        server.expect(requestTo("/fridges/me/" + fridgeCommand.getUserId()))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(mapper.writeValueAsString(fridgeCommand), MediaType.APPLICATION_JSON_UTF8));
        
        FridgeCommand fridge = service.loadByUserId(fridgeCommand.getUserId());
        
        server.verify();
        assertThat(fridge).isEqualToComparingFieldByField(fridgeCommand);
    }
    
    @Test
    public void generate() throws JsonProcessingException {
        FridgeCommand requestFridge = new FridgeCommand();
        requestFridge.setNickname(fridgeCommand.getNickname());
        requestFridge.setUserId(fridgeCommand.getUserId());
        
        URI location = UriComponentsBuilder.fromUriString("/fridges/{id}").buildAndExpand(1).toUri();
        server.expect(requestTo("/fridges"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(mapper.writeValueAsString(requestFridge)))
            .andRespond(withCreatedEntity(location).body(mapper.writeValueAsString(fridgeCommand)).contentType(MediaType.APPLICATION_JSON_UTF8));
        
        FridgeCommand fridge = service.generate(requestFridge.getNickname(), requestFridge.getUserId());
        
        server.verify();
        assertThat(fridge).isEqualToComparingFieldByField(fridgeCommand);
    }
    
}
