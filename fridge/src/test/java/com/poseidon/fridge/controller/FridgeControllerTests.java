package com.poseidon.fridge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.model.Fridge;
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
    private static final String BASE_PATH = "http://localhost";
    
    @Before
    public void setUp() {
        myFridge = new Fridge("myFridge");
        myFridge.setId(ID);
    }
    
    @Test
    public void create() throws Exception {
        when(fridgeService.create(anyString())).thenReturn(myFridge);
        
        final ResultActions resultAction = mvc.perform(post("/fridges")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(myFridge)));
        resultAction.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/fridges/{id:\\d+}"));
        verifyResultActions(resultAction);
    }

    private void verifyResultActions(final ResultActions resultAction) throws Exception {
        resultAction.andExpect(jsonPath("nickname", equalTo(myFridge.getNickname())));
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
            .andExpect(jsonPath("_embedded.fridgeResourceList[0]._links.self.href", equalTo(BASE_PATH + "/fridges/" + myFridge.getId().intValue())));
    }
    
}
