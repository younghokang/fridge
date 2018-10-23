package com.poseidon.fridge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.poseidon.food.command.Food;
import com.poseidon.fridge.command.Fridge;
import com.poseidon.fridge.service.FridgeClient;
import com.poseidon.member.model.Member;
import com.poseidon.member.model.MemberRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=FridgeController.class)
public class FridgeControllerTests {
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext context;
    
    @MockBean
    private FridgeClient fridgeClient;
    
    private Fridge fridge = Fridge.builder()
            .id(1)
            .nickname("myFridge")
            .userId(1004L)
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
    public void whenLoadMyFridgeThenCreatedFridgeReturn() throws Exception {
        when(fridgeClient.loadByUserId(anyLong())).thenReturn(fridge);
        Resources<Food> resources = new Resources<>(Arrays.asList(Food.builder()
                    .name("Banana")
                    .quantity(1)
                    .expiryDate(LocalDate.now())
                    .build())
                , new Link("/fridges"));
        when(fridgeClient.loadFoodsByFridgeId(anyInt())).thenReturn(resources);
        mockMvc.perform(get("/fridges/me"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("fridges/fridge"))
            .andExpect(model().attribute("fridge", equalTo(fridge)))
            .andDo(print());
    }
    
}
