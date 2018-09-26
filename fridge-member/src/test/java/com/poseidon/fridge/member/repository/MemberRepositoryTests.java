package com.poseidon.fridge.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTests {
    
    @Autowired
    MemberRepository repository;
    
    @Test
    public void findByUsername() {
        String username = "user";
        Member newMember = repository.save(new Member(username, "password"));
        assertThat(newMember).isNotNull();
        assertThat(newMember.getId()).isPositive();
        
        Member member = repository.findByUsername(username)
                .orElse(null);
        assertThat(member).isNotNull();
        assertThat(member).isEqualTo(newMember);
    }

}
