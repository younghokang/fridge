package com.poseidon.fridge.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;
import com.poseidon.fridge.member.service.MemberService;

@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTests {
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private MemberRepository repository;
    
    @MockBean
    private MemberResourceAssembler assembler;
    
    @MockBean
    private MemberService service;
    
    @Autowired
    private ObjectMapper mapper;
    
    private Member member;
    
    private static final String BASE_PATH = "http://localhost";
    
    @Before
    public void setUp() {
        member = new Member("user", "password");
        member.setId(1L);
    }
    
    @Test
    public void findByUsername() throws Exception {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(assembler.toResource(any(Member.class))).thenReturn(new Resource<Member>(member,
                new Link(BASE_PATH + "/members/" + member.getId(), "self")));
        
        final ResultActions resultAction = mvc.perform(get("/members/{username}", member.getUsername()));
        resultAction.andExpect(status().isOk());
        verifyResultActions(resultAction);
    }
    
    private void verifyResultActions(final ResultActions resultAction) throws Exception {
        resultAction.andExpect(jsonPath("id").value(member.getId()));
        resultAction.andExpect(jsonPath("username").value(member.getUsername()));
        resultAction.andExpect(jsonPath("password").value(member.getPassword()));
        resultAction.andExpect(jsonPath("_links.self.href").value(BASE_PATH + "/members/" + member.getId()));
    }
    
    @Test
    public void whenCallFindByUsernameThenThrowUsernameNotFoundException() throws Exception {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        mvc.perform(get("/members/{username}", member.getUsername()))
            .andExpect(status().isNotFound())
            .andExpect(content().string("could not found username by " + member.getUsername()));
    }
    
    @Test
    public void registerNewMember() throws JsonProcessingException, Exception {
        when(service.save(any(Member.class))).thenReturn(member);
        when(assembler.toResource(any(Member.class))).thenReturn(new Resource<Member>(member,
                new Link(BASE_PATH + "/members/" + member.getId(), "self")));
        
        final ResultActions resultAction = mvc.perform(post("/members")
                .content(mapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
        verifyResultActions(resultAction);
    }
    
    @Test
    public void changePassword() throws Exception {
        given(repository.findById(anyLong())).willReturn(Optional.of(member));
        given(service.save(any(Member.class))).willReturn(member);
        given(assembler.toResource(any())).willReturn(new Resource<Member>(member, 
                new Link(BASE_PATH + "/members/" + member.getId()),
                new Link(BASE_PATH + "/members", "members")
                ));
        
        final ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.put("/members/{id}", member.getId())
                .content(mapper.writeValueAsString(member))
                .contentType(MediaTypes.HAL_JSON));
        resultAction.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/members/{id:\\d+}"));
        verifyResultActions(resultAction);
    }
    
    @Test
    public void withdraw() throws Exception {
        doNothing().when(service).withdraw(anyLong());
        URI uri = UriComponentsBuilder.fromUriString("/members/{id}").buildAndExpand(member.getId()).toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaTypes.HAL_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
}
