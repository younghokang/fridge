package com.poseidon.fridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
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
    FridgeService service;
    
    @Test
    public void createWithUserId() {
        String nickname = "myFridge";
        long userId = 1004L;
        Fridge newFridge = Fridge.builder()
                .nickname(nickname)
                .userId(userId)
                .build();
        
        when(service.create(anyString(), anyLong())).thenReturn(newFridge);
        
        Fridge fridge = service.create(nickname, userId);
        
        verify(service, times(1)).create(nickname, userId);
        
        assertThat(fridge).isSameAs(newFridge);
    }

}
