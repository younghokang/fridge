package com.poseidon.fridge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    
    private Fridge myFridge;
    
    @Before
    public void setUp() {
        myFridge = new Fridge("myFridge");
        myFridge.setId(1);
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

}
