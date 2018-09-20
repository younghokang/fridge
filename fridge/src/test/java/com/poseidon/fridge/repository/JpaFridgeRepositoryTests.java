package com.poseidon.fridge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.food.model.Food;
import com.poseidon.fridge.model.Fridge;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaFridgeRepositoryTests {
    
    @Autowired
    JpaFridgeRepository jpaFridgeRepository;
    
    String nickname = "myFridge";
    Fridge fridge = Fridge.builder()
            .nickname(nickname)
            .build();
    
    @Test
    public void createFridge() {
        Fridge newFridge = jpaFridgeRepository.save(fridge);
        assertThat(newFridge).isNotNull();
        assertThat(newFridge.getNickname()).isEqualTo(nickname);
        assertThat(newFridge.getNickname()).isNotEqualTo("another nickname");
    }
    
    @Test
    public void update() {
        Fridge updatedFridge = jpaFridgeRepository.save(fridge);
        assertThat(jpaFridgeRepository.findOne(updatedFridge.getId())).isNotNull();
        
        Fridge replaceFridge = Fridge.builder()
                .id(updatedFridge.getId())
                .nickname("otherFridge")
                .build();
        jpaFridgeRepository.save(replaceFridge);
        
        Fridge savedFridge = jpaFridgeRepository.findOne(replaceFridge.getId());
        assertThat(savedFridge.getNickname()).isEqualTo(replaceFridge.getNickname());
    }
    
    @Test
    public void remove() {
        Fridge newFridge = jpaFridgeRepository.save(fridge);
        assertThat(jpaFridgeRepository.findOne(newFridge.getId())).isNotNull();
        assertThat(jpaFridgeRepository.count()).isEqualTo(1L);
        
        jpaFridgeRepository.delete(newFridge.getId());
        assertThat(jpaFridgeRepository.count()).isZero();
    }
    
    @Test
    public void createFridgeAndAddFoods() {
        Fridge newFridge = jpaFridgeRepository.save(fridge);
        assertThat(newFridge.getFoods()).isNullOrEmpty();
        
        Food milk = Food.builder()
                .name("파스퇴르 우유 1.8L")
                .quantity(1)
                .build();
        
        newFridge.addFood(milk);
        jpaFridgeRepository.flush();
        
        Fridge dbFridge = jpaFridgeRepository.findOne(newFridge.getId());
        assertThat(dbFridge.getFoods().size()).isEqualTo(1);
        assertThat(dbFridge.getFoods().get(0).getId()).isPositive();
        
        dbFridge.removeFood(milk);
        jpaFridgeRepository.flush();
        
        assertThat(dbFridge.getFoods().size()).isZero();
    }
    
    @Test
    public void findByUserId() {
        long firstUserId = 1004L;
        fridge.setUserId(firstUserId);
        
        jpaFridgeRepository.save(fridge);
        
        Fridge dbFirstFridge = jpaFridgeRepository.findByUserId(firstUserId);
        assertThat(dbFirstFridge).isSameAs(fridge);
        
        Fridge notExistFridge = jpaFridgeRepository.findByUserId(Long.MAX_VALUE);
        assertThat(notExistFridge).isNull();
    }
    
    @Test(expected=IncorrectResultSizeDataAccessException.class)
    public void tooManyResult() {
        long userId = 1004L;
        Fridge fridge1 = Fridge.builder().nickname("fridge1").userId(userId).build();
        Fridge fridge2 = Fridge.builder().nickname("fridge2").userId(userId).build();
        jpaFridgeRepository.save(fridge1);
        jpaFridgeRepository.save(fridge2);
        
        jpaFridgeRepository.findByUserId(userId);
    }

}
