package com.poseidon.fridge.member.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTests {
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private MemberRepository repository;
    
    @MockBean
    private MemberResourceAssembler assembler;
    
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
    
}
