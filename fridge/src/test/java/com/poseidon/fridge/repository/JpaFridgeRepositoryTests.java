package com.poseidon.fridge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.fridge.model.Fridge;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaFridgeRepositoryTests {
    
    @Autowired
    JpaFridgeRepository jpaFridgeRepository;
    
    @Test
    public void createFridge() {
        String nickname = "myFridge";
        Fridge fridge = jpaFridgeRepository.save(new Fridge(nickname));
        assertThat(fridge).isNotNull();
        assertThat(fridge.getNickname()).isEqualTo(nickname);
        assertThat(fridge.getNickname()).isNotEqualTo("another nickname");
    }

}
