package com.poseidon.food.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.food.model.Food;
import com.poseidon.food.model.FoodRequest;
import com.poseidon.food.repository.FoodRepository;
import com.poseidon.food.service.FoodService;
import com.poseidon.fridge.model.Fridge;

@RunWith(SpringRunner.class)
@WebMvcTest(FoodControllerTests.class)
public class FoodControllerTests {
    
    @Configuration
    @ComponentScan(basePackageClasses = {FoodControllerTests.class})
    public static class TestConf {}
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();
    
    @MockBean
    private FoodRepository repository;
    
    @MockBean
    private FoodService service;
    
    @MockBean
    private FoodResourceAssembler assembler;
    
    private Food milk = Food.builder()
            .id(ID)
            .name("파스퇴르 우유 1.8L")
            .quantity(1)
            .fridge(Fridge.builder().id(1).nickname("myFridge").build())
            .build();
    
    FoodRequest foodRequest = new FoodRequest(milk);
    
    private static final Long ID = 1L;
    private static final String BASE_PATH = "http://localhost";
    
    @Test
    public void findAllFoods() throws Exception {
        List<Food> foods = Arrays.asList(milk);
        given(repository.findAll()).willReturn(foods);
        given(assembler.toResource(any(Food.class))).willReturn(new Resource<Food>(milk,
                new Link(BASE_PATH + "/foods/" + milk.getId()),
                new Link(BASE_PATH + "/foods", "foods")));
        
        final ResultActions result = mvc.perform(get("/foods")
                .accept(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/foods")))
            .andExpect(jsonPath("_embedded.foodList[0].id", equalTo(milk.getId().intValue())))
            .andExpect(jsonPath("_embedded.foodList[0].name", equalTo(milk.getName())))
            .andExpect(jsonPath("_embedded.foodList[0].quantity", equalTo(milk.getQuantity())))
            .andExpect(jsonPath("_embedded.foodList[0].expiryDate", equalTo(milk.getExpiryDate().toString())))
            .andExpect(jsonPath("_embedded.foodList[0]._links.self.href", equalTo(BASE_PATH +"/foods/" + milk.getId().intValue())));
    }
    
    @Test
    public void findById() throws Exception {
        given(repository.findById(ID)).willReturn(Optional.of(milk));
        given(assembler.toResource(any(Food.class))).willReturn(new Resource<Food>(milk,
                new Link(BASE_PATH + "/foods/" + milk.getId()),
                new Link(BASE_PATH + "/foods", "foods")));
        
        URI uri = UriComponentsBuilder
                .fromUriString("/foods/{id}")
                .buildAndExpand(ID)
                .toUri();
        
        final ResultActions result = mvc.perform(get(uri));
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
        given(service.save(any(Food.class))).willReturn(milk);
        given(assembler.toResource(any(Food.class))).willReturn(new Resource<Food>(milk,
                new Link(BASE_PATH + "/foods/" + milk.getId()),
                new Link(BASE_PATH + "/foods", "foods")));
        
        final ResultActions result = mvc.perform(post("/foods")
                .content(mapper.writeValueAsString(foodRequest))
                .contentType(MediaTypes.HAL_JSON));
        result.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/foods/{id:\\d+}"));
        verifyResultContent(result);
    }
    
    @Test
    public void put() throws Exception {
        given(repository.findById(anyLong())).willReturn(Optional.of(milk));
        given(service.save(any(Food.class))).willReturn(milk);
        given(assembler.toResource(any(Food.class))).willReturn(new Resource<Food>(milk,
                new Link(BASE_PATH + "/foods/" + milk.getId()),
                new Link(BASE_PATH + "/foods", "foods")));
        
        URI uri = UriComponentsBuilder.fromUriString("/foods/{id}").buildAndExpand(ID).toUri();
        final ResultActions result = mvc.perform(MockMvcRequestBuilders.put(uri)
                .content(mapper.writeValueAsString(foodRequest))
                .contentType(MediaTypes.HAL_JSON));
        result.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/foods/{id:\\d+}"));
        verifyResultContent(result);
    }
    
    @Test
    public void delete() throws Exception {
        given(repository.findOne(anyLong())).willReturn(milk);
        URI uri = UriComponentsBuilder.fromUriString("/foods/{id}").buildAndExpand(ID).toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaTypes.HAL_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
    @Test
    public void deleteAll() throws Exception {
        doNothing().when(service).removeAll();
        URI uri = UriComponentsBuilder.fromUriString("/foods").build().toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaTypes.HAL_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
        verify(service, times(1)).removeAll();
    }
    
}
