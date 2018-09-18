package com.poseidon.fridge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.food.model.Food;
import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.model.FridgeRequest;
import com.poseidon.fridge.repository.JpaFridgeRepository;
import com.poseidon.fridge.service.FridgeService;

@RunWith(SpringRunner.class)
@WebMvcTest(FridgeController.class)
public class FridgeControllerTests {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();
    
    @MockBean
    private FridgeService fridgeService;
    
    @MockBean
    private JpaFridgeRepository fridgeRepository;
    
    private Fridge myFridge;
    private static final Integer ID = 1;
    private static final Long USER_ID = 1004L;
    private static final String BASE_PATH = "http://localhost";
    
    @Before
    public void setUp() {
        myFridge = new Fridge("myFridge");
        myFridge.setId(ID);
        myFridge.setUserId(USER_ID);
        myFridge.addFood(new Food.Builder("파스퇴르 우유 1.8L", 1).build());
    }
    
    @Test
    public void create() throws Exception {
        when(fridgeService.create(anyString(), anyLong())).thenReturn(myFridge);
        
        final ResultActions resultAction = mvc.perform(post("/fridges")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(myFridge)));
        resultAction.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/fridges/{id:\\d+}"));
        verifyResultActions(resultAction);
    }

    private void verifyResultActions(final ResultActions resultAction) throws Exception {
        resultAction.andExpect(jsonPath("nickname", equalTo(myFridge.getNickname())));
        resultAction.andExpect(jsonPath("foods[0].name", equalTo(myFridge.getFoods().get(0).getName())));
        resultAction.andExpect(jsonPath("foods[0].quantity", equalTo(myFridge.getFoods().get(0).getQuantity())));
        resultAction.andExpect(jsonPath("foods[0].expiryDate", equalTo(myFridge.getFoods().get(0).getExpiryDate().toString())));
    }
    
    @Test
    public void loadFridgeById() throws Exception {
        when(fridgeRepository.findOne(ID)).thenReturn(myFridge);
        final ResultActions resultAction = mvc.perform(get("/fridges/" + ID));
        resultAction.andExpect(status().isOk());
        verifyResultActions(resultAction);
    }
    
    @Test
    public void findAllFridges() throws Exception {
        List<Fridge> fridges = Arrays.asList(myFridge);
        given(fridgeRepository.findAll()).willReturn(fridges);
        
        final ResultActions result = mvc.perform(get("/fridges").accept(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/fridges")))
            .andExpect(jsonPath("_embedded.fridgeResourceList[0].id", equalTo(myFridge.getId().intValue())))
            .andExpect(jsonPath("_embedded.fridgeResourceList[0].nickname", equalTo(myFridge.getNickname())))
            .andExpect(jsonPath("_embedded.fridgeResourceList[0].foods[0].name", equalTo(myFridge.getFoods().get(0).getName())))
            .andExpect(jsonPath("_embedded.fridgeResourceList[0].foods[0].quantity", equalTo(myFridge.getFoods().get(0).getQuantity())))
            .andExpect(jsonPath("_embedded.fridgeResourceList[0].foods[0].expiryDate", equalTo(myFridge.getFoods().get(0).getExpiryDate().toString())))
            .andExpect(jsonPath("_embedded.fridgeResourceList[0]._links.self.href", equalTo(BASE_PATH + "/fridges/" + myFridge.getId().intValue())));
    }
    
    @Test
    public void put() throws Exception {
        given(fridgeRepository.findOne(anyInt())).willReturn(myFridge);
        given(fridgeService.save(any(Fridge.class))).willReturn(myFridge);
        
        FridgeRequest fridgeRequest = new FridgeRequest();
        fridgeRequest.setId(myFridge.getId());
        fridgeRequest.setNickname(myFridge.getNickname());
        fridgeRequest.setUserId(myFridge.getUserId());
        fridgeRequest.setFoods(myFridge.getFoods());
        
        URI uri = UriComponentsBuilder.fromUriString("/fridges/{id}").buildAndExpand(ID).toUri();
        mvc.perform(MockMvcRequestBuilders.put(uri)
                .content(mapper.writeValueAsString(fridgeRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
    @Test
    public void delete() throws Exception {
        given(fridgeRepository.findOne(anyInt())).willReturn(myFridge);
        URI uri = UriComponentsBuilder.fromUriString("/fridges/{id}").buildAndExpand(ID).toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
    @Test
    public void deleteAll() throws Exception {
        doNothing().when(fridgeService).removeAll();
        URI uri = UriComponentsBuilder.fromUriString("/fridges").build().toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
        verify(fridgeService, times(1)).removeAll();
    }
    
    @Test
    public void loadMyFridge() throws Exception {
        given(fridgeRepository.findByUserId(anyLong())).willReturn(myFridge);
        
        URI uri = UriComponentsBuilder.fromUriString("/fridges/me/{userId}").buildAndExpand(USER_ID).toUri();
        final ResultActions resultAction = mvc.perform(get(uri));
        resultAction.andExpect(status().isOk());
        verifyResultActions(resultAction);
    }
    
}
