package com.poseidon.food.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.poseidon.food.command.FoodCommand;
import com.poseidon.fridge.command.FridgeCommand;
import com.poseidon.fridge.service.FridgeClient;
import com.poseidon.member.model.Member;
import com.poseidon.member.model.MemberRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=FoodController.class)
public class FoodControllerTests {
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext context;
    
    @MockBean
    private FridgeClient fridgeClient;
    
    private FridgeCommand fridge = FridgeCommand.builder()
            .id(1)
            .nickname("myFridge")
            .userId(1004L)
            .build();
    
    private FoodCommand food = FoodCommand.builder()
            .id(1L)
            .name("Banana")
            .quantity(12)
            .expiryDate(LocalDate.now())
            .fridgeId(fridge.getId())
            .build();
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setId(1004L);
        memberRequest.setUsername("user@example.com");
        memberRequest.setPassword("password");
        memberRequest.generateNewUser(new BCryptPasswordEncoder());
        
        Member member = memberRequest.toMember();
        TestingAuthenticationToken token = new TestingAuthenticationToken(member, null);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
    
    @Test
    public void whenRegisterFoodSuccessThenRedirect() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fridgeId", food.getFridgeId().toString());
        params.add("name", food.getName());
        params.add("quantity", food.getQuantity().toString());
        
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("foodCommand", food);
        
        when(fridgeClient.createFood(any(FoodCommand.class))).thenReturn(food);
        
        mockMvc.perform(post("/fridges/{fridgeId}/foods/add", fridge.getId())
                .sessionAttrs(sessionAttributes)
                .params(params))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/fridges/me"))
            .andDo(print());
    }
    
    @Test
    public void whenLoadFoodUpdateFormPageThenExistsModelData() throws Exception {
        when(fridgeClient.loadFoodById(anyLong())).thenReturn(food);
        mockMvc.perform(get("/fridges/{fridgeId}/foods/{id}", fridge.getId(), food.getId()))
            .andExpect(status().isOk())
            .andExpect(model().attribute("foodCommand", food))
            .andExpect(view().name("foods/updateFoodForm"))
            .andDo(print());
    }
    
    @Test
    public void whenUpdateFoodSuccessThenRedirect() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fridgeId", food.getFridgeId().toString());
        params.add("name", food.getName());
        params.add("quantity", food.getQuantity().toString());
        
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("foodCommand", food);
        
        when(fridgeClient.updateFood(anyLong(), any(FoodCommand.class))).thenReturn(food);
        
        mockMvc.perform(put("/fridges/{fridgeId}/foods/{id}", fridge.getId(), food.getId())
                .sessionAttrs(sessionAttributes)
                .params(params))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/fridges/me"))
            .andDo(print());
    }
    
    @Test
    public void whenDeleteFoodSuccessThenRedirect() throws Exception {
        doNothing().when(fridgeClient).deleteFood(anyLong());
        
        mockMvc.perform(get("/fridges/{fridgeId}/foods/delete/{id}", fridge.getId(), food.getId()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/fridges/me"))
            .andDo(print());
        
        verify(fridgeClient, times(1)).deleteFood(food.getId());
    }
    
}
