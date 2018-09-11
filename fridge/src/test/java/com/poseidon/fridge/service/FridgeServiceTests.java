package com.poseidon.fridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poseidon.fridge.model.Fridge;

@RunWith(MockitoJUnitRunner.class)
public class FridgeServiceTests {
    
    @Mock
    FridgeService fridgeService;
    
    @Test
    public void create() {
        String nickname = "myFridge";
        when(fridgeService.create(anyString())).thenReturn(new Fridge(nickname));
        Fridge fridge = fridgeService.create(nickname);
        
        verify(fridgeService, times(1)).create(nickname);
        
        assertThat(fridge.getNickname()).isEqualTo(nickname);
    }

}
