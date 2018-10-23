package com.poseidon.fridge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.model.Fridge;
import com.poseidon.fridge.repository.FridgeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest({"eureka.client.enabled:false"})
public class FridgeClient {
    
    @Autowired WebApplicationContext context;
    
    private MockMvc mvc;
    
    @Autowired ObjectMapper mapper;
    
    @Autowired FridgeRepository repository;
    
    private Fridge fridge;
    
    private static final String BASE_PATH = "http://localhost";
    
    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
        
        fridge = Fridge.builder()
                .nickname("myFridge")
                .userId(1004L)
                .build();
        repository.save(fridge);
    }
    
    @Test
    public void findByUserId() throws Exception {
        final ResultActions result = mvc.perform(get("/fridges/search/findByUserId?userId={userId}", fridge.getUserId())
                .accept(MediaTypes.HAL_JSON));
        result.andExpect(status().isOk());
        verifyResultContent(result);
    }
    
    private void verifyResultContent(final ResultActions result) throws Exception {
        result
            .andExpect(jsonPath("id", equalTo(fridge.getId().intValue())))
            .andExpect(jsonPath("nickname", equalTo(fridge.getNickname())))
            .andExpect(jsonPath("userId", equalTo(fridge.getUserId().intValue())))
            .andExpect(jsonPath("_links").isMap())
            .andExpect(jsonPath("_links.self").isNotEmpty())
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/fridges/" + fridge.getId().intValue())));
    }
    
    @Test
    public void createFridge() throws Exception {
        final ResultActions result = mvc.perform(post("/fridges")
                .contentType(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(fridge)));
        result.andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/fridges/{id:\\d+}"))
            .andDo(print());
    }

    @Test
    public void loadFridgeById() throws Exception {
        final ResultActions resultAction = mvc.perform(get("/fridges/{id}", fridge.getId())
                .contentType(MediaTypes.HAL_JSON));
        resultAction.andExpect(status().isOk());
        verifyResultContent(resultAction);
    }
    
    @Test
    public void notFoundFridge() throws Exception {
        mvc.perform(get("/fridges/{id}", Integer.MAX_VALUE)
                .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    
    @Test
    public void findAllFridges() throws Exception {
        final ResultActions result = mvc.perform(get("/fridges")
                .accept(MediaTypes.HAL_JSON));
        result
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
            .andExpect(jsonPath("_links").isMap())
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/fridges{?page,size,sort}")))
            .andExpect(jsonPath("_embedded").isMap())
            .andExpect(jsonPath("_embedded.fridges").isArray())
            .andExpect(jsonPath("_embedded.fridges[0].id", equalTo(fridge.getId().intValue())))
            .andExpect(jsonPath("_embedded.fridges[0].nickname", equalTo(fridge.getNickname())))
            .andExpect(jsonPath("_embedded.fridges[0]._links.self.href", equalTo(BASE_PATH + "/fridges/" + fridge.getId().intValue())));
    }
    
    @Test
    public void updateFridge() throws Exception {
        fridge.setNickname("myNickname");
        final ResultActions resultAction = mvc.perform(put("/fridges/{id}", fridge.getId())
                .content(mapper.writeValueAsString(fridge))
                .contentType(MediaTypes.HAL_JSON));
        resultAction.andExpect(status().isNoContent())
            .andExpect(redirectedUrlPattern("**/fridges/{id:\\d+}"));
    }
    
    @Test
    public void deleteFridge() throws Exception {
        mvc.perform(delete("/fridges/{id}", fridge.getId())
                .contentType(MediaTypes.HAL_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""))
            .andDo(print());
    }
    
}
