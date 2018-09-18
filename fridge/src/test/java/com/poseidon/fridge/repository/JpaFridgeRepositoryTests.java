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
    
    @Test
    public void createFridge() {
        String nickname = "myFridge";
        Fridge fridge = jpaFridgeRepository.save(new Fridge(nickname));
        assertThat(fridge).isNotNull();
        assertThat(fridge.getNickname()).isEqualTo(nickname);
        assertThat(fridge.getNickname()).isNotEqualTo("another nickname");
    }
    
    @Test
    public void update() {
        Fridge fridge = jpaFridgeRepository.save(new Fridge("myFridge"));
        assertThat(jpaFridgeRepository.findOne(fridge.getId())).isNotNull();
        
        Fridge changeNicknameFridge = new Fridge("otherFridge");
        changeNicknameFridge.setId(fridge.getId());
        jpaFridgeRepository.save(changeNicknameFridge);
        
        Fridge savedFridge = jpaFridgeRepository.findOne(fridge.getId());
        assertThat(savedFridge.getNickname()).isEqualTo(changeNicknameFridge.getNickname());
    }
    
    @Test
    public void remove() {
        Fridge fridge = jpaFridgeRepository.save(new Fridge("myFridge"));
        assertThat(jpaFridgeRepository.findOne(fridge.getId())).isNotNull();
        assertThat(jpaFridgeRepository.count()).isEqualTo(1L);
        
        jpaFridgeRepository.delete(fridge.getId());
        assertThat(jpaFridgeRepository.count()).isZero();
    }
    
    @Test
    public void createFridgeAndAddFoods() {
        String nickname = "myFridge";
        Fridge fridge = jpaFridgeRepository.save(new Fridge(nickname));
        assertThat(fridge.hasFood()).isFalse();
        
        Food milk = new Food.Builder("파스퇴르 우유 1.8L", 1).build();
        fridge.addFood(milk);
        jpaFridgeRepository.flush();
        
        Fridge dbFridge = jpaFridgeRepository.findOne(fridge.getId());
        assertThat(dbFridge.hasFood()).isTrue();
        assertThat(dbFridge.getFoods().size()).isEqualTo(1);
        assertThat(dbFridge.getFoods().get(0).getId()).isPositive();
        
        dbFridge.removeFood(milk);
        jpaFridgeRepository.flush();
        
        assertThat(dbFridge.hasFood()).isFalse();
        assertThat(dbFridge.getFoods().size()).isZero();
    }
    
    @Test
    public void findByUserId() {
        long firstUserId = 1004L;
        Fridge firstFridge = new Fridge("firstFridge");
        firstFridge.setUserId(firstUserId);
        jpaFridgeRepository.save(firstFridge);
        
        Fridge dbFirstFridge = jpaFridgeRepository.findByUserId(firstUserId);
        assertThat(dbFirstFridge).isSameAs(firstFridge);
        
        Fridge notExistFridge = jpaFridgeRepository.findByUserId(Long.MAX_VALUE);
        assertThat(notExistFridge).isNull();
    }
    
    @Test(expected=IncorrectResultSizeDataAccessException.class)
    public void tooManyResult() {
        long userId = 1004L;
        Fridge fridge1 = new Fridge("fridge1");
        Fridge fridge2 = new Fridge("fridge2");
        fridge1.setUserId(userId);
        fridge2.setUserId(userId);
        jpaFridgeRepository.save(fridge1);
        jpaFridgeRepository.save(fridge2);
        
        jpaFridgeRepository.findByUserId(userId);
    }

}
