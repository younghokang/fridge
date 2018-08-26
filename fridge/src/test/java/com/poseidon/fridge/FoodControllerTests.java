package com.poseidon.fridge;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.fridge.controller.FoodController;
import com.poseidon.fridge.model.Food;
import com.poseidon.fridge.repository.JpaFoodRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(FoodController.class)
public class FoodControllerTests {
    
    @Autowired
    private MockMvc mvc;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @MockBean
    JpaFoodRepository jpaFoodRepository;
    
    @Test
    public void findAllFoods() throws Exception {
        List<Food> foods = Arrays.asList(new Food("파스퇴르 우유 1.8L", 1, "2018-09-28"));
        given(jpaFoodRepository.findAll()).willReturn(foods);
        
        String expectedJson = mapper.writeValueAsString(foods);
        mvc.perform(get("/foods").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(expectedJson));
    }
    
}
