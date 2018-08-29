package com.poseidon.fridge;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.controller.FoodController;
import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JpaFoodRepository;
import com.poseidon.fridge.service.JpaFoodService;

@RunWith(SpringRunner.class)
@WebMvcTest(FoodController.class)
public class FoodControllerTests {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();
    
    @MockBean
    private JpaFoodRepository jpaFoodRepository;
    
    @MockBean
    private JpaFoodService jpaFoodService;
    
    private Food milk;
    private static final Long ID = 1L;
    private static final String BASE_PATH = "http://localhost";
    
    @Before
    public void setUp() {
        milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
        milk.setId(ID);
    }
    
    @Test
    public void findAllFoods() throws Exception {
        List<Food> foods = Arrays.asList(milk);
        given(jpaFoodRepository.findAll()).willReturn(foods);
        
        final ResultActions result = mvc.perform(get("/foods").accept(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/foods")))
            .andExpect(jsonPath("_embedded.foodResourceList[0].id", equalTo(milk.getId().intValue())))
            .andExpect(jsonPath("_embedded.foodResourceList[0].name", equalTo(milk.getName())))
            .andExpect(jsonPath("_embedded.foodResourceList[0].quantity", equalTo(milk.getQuantity())))
            .andExpect(jsonPath("_embedded.foodResourceList[0].expiryDate", equalTo(milk.getExpiryDate())))
            .andExpect(jsonPath("_embedded.foodResourceList[0]._links.self.href", equalTo(BASE_PATH + "/foods/" + milk.getId().intValue())));
    }
    
    @Test
    public void findById() throws Exception {
        given(jpaFoodRepository.findOne(ID)).willReturn(milk);
        final ResultActions result = mvc.perform(get("/foods/" + ID));
        result.andExpect(status().isOk());
        verifyResultContent(result);
    }
    
    private void verifyResultContent(final ResultActions result) throws Exception {
        result
            .andExpect(jsonPath("id", equalTo(milk.getId().intValue())))
            .andExpect(jsonPath("name", equalTo(milk.getName())))
            .andExpect(jsonPath("quantity", equalTo(milk.getQuantity())))
            .andExpect(jsonPath("expiryDate", equalTo(milk.getExpiryDate())))
            .andExpect(jsonPath("_links.self.href", equalTo(BASE_PATH + "/foods/" + milk.getId())));
    }

    @Test
    public void postSave() throws Exception {
        given(jpaFoodService.save(any(Food.class))).willReturn(milk);
        mvc.perform(post("/foods")
                .content(mapper.writeValueAsString(milk))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated())
            .andExpect(content().json(mapper.writeValueAsString(milk)))
            .andExpect(redirectedUrlPattern("**/foods/{id:\\d+}"));
    }
    
    @Test
    public void put() throws Exception {
        given(jpaFoodRepository.findOne(anyLong())).willReturn(milk);
        given(jpaFoodService.save(any(Food.class))).willReturn(milk);
        URI uri = UriComponentsBuilder.fromUriString("/foods/{id}").buildAndExpand(ID).toUri();
        mvc.perform(MockMvcRequestBuilders.put(uri)
                .content(mapper.writeValueAsString(milk))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
    @Test
    public void delete() throws Exception {
        given(jpaFoodRepository.findOne(anyLong())).willReturn(milk);
        URI uri = UriComponentsBuilder.fromUriString("/foods/{id}").buildAndExpand(ID).toUri();
        mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
    
}
