package com.poseidon.member.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(properties= {"eureka.client.enabled:false"})
@AutoConfigureMockMvc
public class MemberControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void signinWithInvalidUserThenAuthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("invalid")
                .password("invalidpassword");
        
        mockMvc.perform(login)
            .andExpect(unauthenticated());
    }
    
    @Test
    public void accessUnsecuredResourceThenOk() throws Exception {
        mockMvc.perform(get("/signup"))
            .andExpect(status().isOk());
    }
    
    @Test
    public void accessSecuredResourceUnauthenticated() throws Exception {
        mockMvc.perform(get("/fridges/1/foods/add"))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser
    public void accessSecuredResourceAuthenticatedThenOk() throws Exception {
        mockMvc.perform(get("/fridges/1/foods/add"))
            .andExpect(status().isOk());
    }
    
}
