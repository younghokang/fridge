package com.poseidon.fridge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.model.FridgeRequest;
import com.poseidon.fridge.repository.FridgeRepository;
import com.poseidon.fridge.service.FridgeService;

@RunWith(SpringRunner.class)
@WebMvcTest(FridgeController.class)
public class FridgeControllerTests {
    
    @Configuration
    @ComponentScan(basePackageClasses = {FridgeController.class})
    public static class TestConf {}
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper mapper;
    
    @MockBean
    private FridgeService service;
    
    @MockBean
    private FridgeRepository repository;
    
    @MockBean
    private FridgeResourceAssembler assembler;
    
    private static final Integer ID = 1;
    private static final Long USER_ID = 1004L;
    private static final String BASE_PATH = "http://localhost";
    private Fridge fridge = Fridge.builder()
            .id(ID)
            .nickname("myFridge")
            .userId(USER_ID)
            .build();
    private FridgeRequest myFridge = new FridgeRequest(fridge);
    
    @Test
    public void create() throws Exception {
        given(service.create(anyString(), anyLong())).willReturn(fridge);
        given(assembler.toResource(any(Fridge.class))).willReturn(new Resource<Fridge>(fridge, 
                new Link(BASE_PATH + "/fridges/" + fridge.getId()),
                new Link(BASE_PATH + "/fridges", "fridges")
                ));
        
        final ResultActions resultAction = mvc.perform(post("/fridges")
                .contentType(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(new FridgeRequest(fridge))));
        resultAction.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/fridges/{id:\\d+}"));
        verifyResultActions(resultAction);
    }

    private void verifyResultActions(final ResultActions resultAction) throws Exception {
        resultAction.andExpect(jsonPath("id").value(myFridge.getId()));
        resultAction.andExpect(jsonPath("nickname").value(myFridge.getNickname()));
        resultAction.andExpect(jsonPath("userId").value(myFridge.getUserId()));
        resultAction.andExpect(jsonPath("_links").isMap());
        resultAction.andExpect(jsonPath("_links.self").isNotEmpty());
        resultAction.andExpect(jsonPath("_links.self.href").value(BASE_PATH + "/fridges/" + myFridge.getId()));
        resultAction.andExpect(jsonPath("_links.fridges").isNotEmpty());
        resultAction.andExpect(jsonPath("_links.fridges.href").value(BASE_PATH + "/fridges"));
    }
    
    @Test
    public void loadFridgeById() throws Exception {
        given(repository.findById(ID)).willReturn(Optional.of(fridge));
        given(assembler.toResource(any(Fridge.class))).willReturn(new Resource<Fridge>(fridge, 
                new Link(BASE_PATH + "/fridges/" + fridge.getId()),
                new Link(BASE_PATH + "/fridges", "fridges")
                ));
        
        final ResultActions resultAction = mvc.perform(get("/fridges/" + ID));
        resultAction.andExpect(status().isOk());
        verifyResultActions(resultAction);
    }
    
    @Test
    public void notFoundFridge() throws Exception {
        given(repository.findById(ID)).willReturn(Optional.empty());
        mvc.perform(get("/fridges/" + ID))
            .andExpect(status().isNotFound())
            .andExpect(content().string("could not found fridge #" + ID));
    }
    
    @Test
    public void findAllFridges() throws Exception {
        List<Fridge> fridges = Arrays.asList(fridge);
        given(repository.findAll()).willReturn(fridges);
        given(assembler.toResource(any(Fridge.class))).willReturn(new Resource<Fridge>(fridge, 
                new Link(BASE_PATH + "/fridges/" + fridge.getId()),
                new Link(BASE_PATH + "/fridges", "fridges")
                ));
        
        final ResultActions result = mvc.perform(get("/fridges").accept(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("_links").isMap())
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/fridges")))
            .andExpect(jsonPath("_embedded").isMap())
            .andExpect(jsonPath("_embedded.fridgeList").isArray())
            .andExpect(jsonPath("_embedded.fridgeList[0].id", equalTo(myFridge.getId().intValue())))
            .andExpect(jsonPath("_embedded.fridgeList[0].nickname", equalTo(myFridge.getNickname())))
            .andExpect(jsonPath("_embedded.fridgeList[0]._links.self.href", equalTo(BASE_PATH + "/fridges/" + myFridge.getId().intValue())));
    }
    
    @Test
    public void put() throws Exception {
        given(repository.findById(anyInt())).willReturn(Optional.of(fridge));
        given(service.save(any(Fridge.class))).willReturn(fridge);
        given(assembler.toResource(any())).willReturn(new Resource<Fridge>(fridge, 
                new Link(BASE_PATH + "/fridges/" + fridge.getId()),
                new Link(BASE_PATH + "/fridges", "fridges")
                ));
        
        final ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.put("/fridges/{id}", ID)
                .content(mapper.writeValueAsString(myFridge))
                .contentType(MediaTypes.HAL_JSON));
        resultAction.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/fridges/{id:\\d+}"));
        verifyResultActions(resultAction);
    }
    
    @Test
    public void delete() throws Exception {
        given(repository.findById(anyInt())).willReturn(Optional.of(fridge));
        URI uri = UriComponentsBuilder.fromUriString("/fridges/{id}").buildAndExpand(ID).toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaTypes.HAL_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
    @Test
    public void deleteAll() throws Exception {
        doNothing().when(service).removeAll();
        URI uri = UriComponentsBuilder.fromUriString("/fridges").build().toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
        verify(service, times(1)).removeAll();
    }
    
    @Test
    public void loadMyFridge() throws Exception {
        given(repository.findByUserId(anyLong())).willReturn(Optional.of(fridge));
        given(assembler.toResource(any(Fridge.class))).willReturn(new Resource<Fridge>(fridge, 
                new Link(BASE_PATH + "/fridges/" + fridge.getId()),
                new Link(BASE_PATH + "/fridges", "fridges")
                ));
        
        URI uri = UriComponentsBuilder.fromUriString("/fridges/me/{userId}").buildAndExpand(USER_ID).toUri();
        final ResultActions resultAction = mvc.perform(get(uri));
        resultAction.andExpect(status().isOk());
        verifyResultActions(resultAction);
    }
    
}
