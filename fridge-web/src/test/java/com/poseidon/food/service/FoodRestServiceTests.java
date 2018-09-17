package com.poseidon.food.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.util.Date;

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
import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;

@RunWith(SpringRunner.class)
@RestClientTest(FoodRestService.class)
public class FoodRestServiceTests {
    
    @Autowired
    FoodRestService service;
    
    @Autowired
    private MockRestServiceServer server;
    
    @Autowired
    private ObjectMapper mapper;
    
    private FridgeCommand fridgeCommand;
    private FoodCommand foodCommand;
    
    @Before
    public void setUp() {
        fridgeCommand = new FridgeCommand();
        fridgeCommand.setId(1);
        fridgeCommand.setNickname("myFridge");
        fridgeCommand.setUserId(1004L);
        
        foodCommand = new FoodCommand();
        foodCommand.setId(1L);
        foodCommand.setName("Banana Cake");
        foodCommand.setQuantity(3);
        foodCommand.setExpiryDate(new Date());
        foodCommand.setFridge(fridgeCommand);
    }
    
    @Test
    public void create() throws JsonProcessingException {
        URI location = UriComponentsBuilder.fromUriString("/foods/{id}").buildAndExpand(foodCommand.getId()).toUri();
        
        server.expect(requestTo("/foods"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(mapper.writeValueAsString(foodCommand)))
            .andRespond(withCreatedEntity(location).body(mapper.writeValueAsString(foodCommand)).contentType(MediaType.APPLICATION_JSON_UTF8));
        
        FoodCommand food = service.create(foodCommand);
        
        server.verify();
        assertThat(food).isEqualToComparingFieldByFieldRecursively(foodCommand);
    }
    
    @Test
    public void loadById() throws JsonProcessingException {
        server.expect(requestTo("/foods/" + foodCommand.getId()))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(mapper.writeValueAsString(foodCommand), MediaType.APPLICATION_JSON_UTF8));
        
        FoodCommand food = service.loadById(foodCommand.getId());
        
        server.verify();
        assertThat(food).isEqualToComparingFieldByFieldRecursively(foodCommand);
    }
    
    @Test
    public void update() throws JsonProcessingException {
        server.expect(requestTo("/foods/" + foodCommand.getId()))
            .andExpect(method(HttpMethod.PUT))
            .andExpect(content().string(mapper.writeValueAsString(foodCommand)))
            .andRespond(withNoContent());
        
        service.update(foodCommand, foodCommand.getId());
        
        server.verify();
    }
    
    @Test
    public void delete() {
        server.expect(requestTo("/foods/" + foodCommand.getId()))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withNoContent());
        
        service.delete(foodCommand.getId());
        
        server.verify();
    }

}
