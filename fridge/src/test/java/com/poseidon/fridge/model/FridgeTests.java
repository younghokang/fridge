package com.poseidon.fridge.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FridgeTests {
    
    @Test
    public void newFridge() {
        String nickname = "myFridge";
        Fridge myFridge = new Fridge(nickname);
        assertThat(myFridge).isNotNull();
        assertThat(myFridge.getNickname()).isEqualTo(nickname);
        assertThat(myFridge.getNickname()).isNotEqualTo("Blah blah");
    }

}
