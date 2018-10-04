package com.poseidon.food.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.food.command.FoodCommand;

@RunWith(SpringRunner.class)
@RestClientTest(FoodRestService.class)
public class FoodRestServiceTests {
    
    @Autowired
    FoodRestService service;
    
    @Autowired
    private RestTemplate fridgeServiceRestTemplate;
    
    @Autowired
    private MockRestServiceServer server;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Before
    public void setUp() {
        server = MockRestServiceServer.createServer(fridgeServiceRestTemplate);
    }
    
    private FoodCommand food = FoodCommand.builder()
            .id(1L)
            .name("Banana Cake")
            .quantity(3)
            .expiryDate(LocalDate.now())
            .fridgeId(1)
            .build();
    
    private static final String BASE_PATH = "http://fridge-service";
    
    @Test
    public void create() throws JsonProcessingException {
        URI location = UriComponentsBuilder.fromUriString(BASE_PATH + "/foods/{id}").buildAndExpand(food.getId()).toUri();
        
        server.expect(requestTo(BASE_PATH + "/foods"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(mapper.writeValueAsString(food)))
            .andRespond(withCreatedEntity(location).body(mapper.writeValueAsString(food)).contentType(MediaType.APPLICATION_JSON_UTF8));
        
        FoodCommand newFood = service.create(food);
        
        server.verify();
        assertThat(newFood).isEqualToComparingFieldByFieldRecursively(food);
    }
    
    @Test
    public void loadById() throws JsonProcessingException {
        server.expect(requestTo(BASE_PATH + "/foods/" + food.getId()))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(mapper.writeValueAsString(food), MediaType.APPLICATION_JSON_UTF8));
        
        FoodCommand storedFood = service.loadById(food.getId());
        
        server.verify();
        assertThat(storedFood).isEqualToComparingFieldByFieldRecursively(food);
    }
    
    @Test
    public void whenLoadByIdThenNotFoundHttpStatus() {
        server.expect(requestTo(BASE_PATH + "/foods/" + food.getId()))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));
        
        service.loadById(food.getId());
        
        server.verify();
    }
    
    @Test
    public void update() throws JsonProcessingException {
        server.expect(requestTo(BASE_PATH + "/foods/" + food.getId()))
            .andExpect(method(HttpMethod.PUT))
            .andExpect(content().string(mapper.writeValueAsString(food)))
            .andRespond(withNoContent());
        
        service.update(food, food.getId());
        
        server.verify();
    }
    
    @Test
    public void delete() {
        server.expect(requestTo(BASE_PATH + "/foods/" + food.getId()))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withNoContent());
        
        service.delete(food.getId());
        
        server.verify();
    }

}
