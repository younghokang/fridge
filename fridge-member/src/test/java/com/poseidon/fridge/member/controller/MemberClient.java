package com.poseidon.fridge.member.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest({"eureka.client.enabled:false"})
public class MemberClient {
    
    @Autowired WebApplicationContext context;
    
    MockMvc mvc;
    
    @Autowired ObjectMapper mapper;
    
    @Autowired MemberRepository repository;
    
    Member member = new Member();
    
    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
        member.setUsername("bob");
        member.setPassword("1234");
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        member.setAuthorities(authorities);
        
        repository.save(member);
    }
    
    @Test
    public void createNewMember() throws JsonProcessingException, Exception {
        Member member = new Member();
        member.setUsername("alice");
        member.setPassword("1234");
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        member.setAuthorities(authorities);
        
        ResultActions resultActions = mvc.perform(post("/members")
                .content(mapper.writeValueAsString(member))
                .accept(MediaTypes.HAL_JSON));
        resultActions.andExpect(status().isCreated());
    }
    
    @Test
    public void identifiesResourcesUsingUsername() throws JsonProcessingException, Exception {
        ResultActions resultActions = mvc.perform(get("/members/{username}", member.getUsername())
                .accept(MediaTypes.HAL_JSON));
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.username", equalTo(member.getUsername())));
    }
    
    @Test
    public void changePassword() throws Exception {
        ResultActions resultActions = mvc.perform(put("/members/{id}", member.getId())
                .content(mapper.writeValueAsString(member))
                .accept(MediaTypes.HAL_JSON));
        resultActions.andExpect(status().isCreated())
            .andExpect(redirectedUrl("http://localhost/members/" + member.getUsername()));
    }
    
    @Test
    public void withdraw() throws Exception {
        ResultActions resultActions = mvc.perform(delete("/members/{username}", member.getUsername())
                .accept(MediaTypes.HAL_JSON));
        resultActions.andExpect(status().isNoContent());
    }
    
    
}
