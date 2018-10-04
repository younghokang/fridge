package com.poseidon.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.member.model.Member;

@RunWith(SpringRunner.class)
@RestClientTest(MemberRestService.class)
public class MemberRestServiceTests {
    
    @Autowired
    MemberRestService service;
    
    @Autowired
    private RestTemplate memberRestTemplate;
    
    @Autowired
    private MockRestServiceServer server;
    
    @Autowired
    private ObjectMapper mapper;
    
    private Member memberRequest;
    
    private static final String BASE_PATH = "http://fridge-member";
    
    @Before
    public void setUp() {
        server = MockRestServiceServer.createServer(memberRestTemplate);
        memberRequest = new Member();
        memberRequest.setId(1L);
        memberRequest.setUsername("user");
        memberRequest.setPassword("password");
    }
    
    @Test
    public void loadByUsername() throws IOException {
        server.expect(requestTo(BASE_PATH + "/members/" + memberRequest.getUsername()))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(mapper.writeValueAsString(memberRequest), MediaType.APPLICATION_JSON_UTF8));
        
        Member member = service.loadByUsername(memberRequest.getUsername());
        
        server.verify();
        assertThat(member).isEqualToComparingFieldByField(memberRequest);
    }
    
    @Test
    public void newMember() throws JsonProcessingException {
        URI location = UriComponentsBuilder.fromUriString(BASE_PATH + "/members/{id}").buildAndExpand(1).toUri();
        server.expect(requestTo(BASE_PATH + "/members"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(mapper.writeValueAsString(memberRequest)))
            .andRespond(withCreatedEntity(location)
                    .body(mapper.writeValueAsString(memberRequest))
                    .contentType(MediaType.APPLICATION_JSON_UTF8));
        
        Member member = service.register(memberRequest);
        
        server.verify();
        assertThat(member).isEqualToComparingFieldByField(memberRequest);
    }

}
