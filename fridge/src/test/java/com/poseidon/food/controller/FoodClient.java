package com.poseidon.food.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.food.model.Food;
import com.poseidon.food.repository.FoodRepository;
import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.repository.FridgeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest({"eureka.client.enabled:false"})
public class FoodClient {
    
    @Autowired WebApplicationContext context;
    
    private MockMvc mvc;
    
    @Autowired ObjectMapper mapper;
    
    @Autowired FoodRepository repository;
    
    @Autowired FridgeRepository fridgeRepository;
    
    private Fridge fridge;
    private Food milk;
    
    private static final String BASE_PATH = "http://localhost";
    
    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
        
        fridge = Fridge.builder()
                .nickname("myFridge")
                .userId(1004L)
                .build();
        fridgeRepository.save(fridge);
        
        milk = Food.builder()
                .fridge(fridge)
                .name("우유")
                .quantity(1)
                .build();
        repository.save(milk);
    }
    
    @Test
    public void findById() throws Exception {
        final ResultActions result = mvc.perform(get("/foods/{id}", milk.getId()).accept(MediaTypes.HAL_JSON));
        result.andExpect(status().isOk());
        verifyResultContent(result);
    }
    
    private void verifyResultContent(final ResultActions result) throws Exception {
        result
            .andExpect(jsonPath("id", equalTo(milk.getId().intValue())))
            .andExpect(jsonPath("name", equalTo(milk.getName())))
            .andExpect(jsonPath("quantity", equalTo(milk.getQuantity())))
            .andExpect(jsonPath("expiryDate", equalTo(milk.getExpiryDate().toString())))
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/foods/" + milk.getId())));
    }

    @Test
    public void postSave() throws Exception {
        Food food = Food.builder()
                .name("사과")
                .quantity(3)
                .build();
        food.setFridgeId(fridge.getId());
        
        final ResultActions result = mvc.perform(post("/foods")
                .content(mapper.writeValueAsString(food))
                .contentType(MediaTypes.HAL_JSON));
        result.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/foods/{id:\\d+}"))
            .andDo(print());
    }
    
    @Test
    public void updateFood() throws Exception {
        milk.setName("바나나우유");
        milk.setFridgeId(fridge.getId());
        final ResultActions result = mvc.perform(put("/foods/{id}", milk.getId())
                .content(mapper.writeValueAsString(milk))
                .contentType(MediaTypes.HAL_JSON));
        result.andExpect(status().isNoContent())
            .andExpect(redirectedUrlPattern("**/foods/{id:\\d+}"))
            .andDo(print());
    }
    
    @Test
    public void deleteFood() throws Exception {
        mvc.perform(delete("/foods/{id}", milk.getId())
                .contentType(MediaTypes.HAL_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""))
            .andDo(print());
    }
    
}
