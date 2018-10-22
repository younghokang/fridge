package com.poseidon.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.member.model.MemberRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
public class MemberClientTests {
    
    @MockBean
    MemberClient client;
    
    private MemberRequest memberRequest;
    
    @Before
    public void setUp() {
        memberRequest = new MemberRequest();
        memberRequest.setId(1L);
        memberRequest.setUsername("user");
        memberRequest.setPassword("password");
        
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        memberRequest.setAuthorities(authorities);
    }
    
    @Test
    public void loadByUsername() throws IOException {
        given(client.loadByUsername(anyString())).willReturn(memberRequest);
        MemberRequest member = client.loadByUsername(memberRequest.getUsername());
        assertThat(member).isEqualToComparingFieldByField(memberRequest);
    }
    
    @Test(expected=MemberNotFoundException.class)
    public void whenLoadByUsernameThenMemberNotFoundException() {
        given(client.loadByUsername(anyString())).willThrow(MemberNotFoundException.class);
        client.loadByUsername(memberRequest.getUsername());
    }
    
    @Test
    public void newMember() {
        given(client.register(any(MemberRequest.class))).willReturn(memberRequest);
        MemberRequest member = client.register(memberRequest);
        assertThat(member).isEqualToComparingFieldByField(memberRequest);
    }
    
    @Test
    public void changePassword() {
        given(client.changePassword(anyLong(), any(MemberRequest.class))).willReturn(memberRequest);
        MemberRequest member = client.changePassword(memberRequest.getId(), memberRequest);
        assertThat(member).isEqualToComparingFieldByField(memberRequest);
    }
    
    @Test
    public void withdraw() {
        willDoNothing().given(client).withdraw(anyString());
        client.withdraw(memberRequest.getUsername());
        verify(client, times(1)).withdraw(anyString());
    }
    

}
