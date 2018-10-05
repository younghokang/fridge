package com.poseidon.fridge.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.fridge.member.model.Member;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTests {
    
    @Autowired
    MemberRepository repository;
    
    @Test
    public void findByUsername() {
        String username = "user";
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        Member newMember = repository.save(new Member(username, "password", authorities));
        assertThat(newMember).isNotNull();
        assertThat(newMember.getId()).isPositive();
        
        Member member = repository.findByUsername(username)
                .orElse(null);
        assertThat(member).isNotNull();
        assertThat(member).isEqualTo(newMember);
        
        assertThat(member.isAccountNonExpired()).isTrue();
        assertThat(member.isAccountNonLocked()).isTrue();
        assertThat(member.isCredentialsNonExpired()).isTrue();
        assertThat(member.isEnabled()).isTrue();
        
        assertThat(member.getAuthorities().size()).isEqualTo(authorities.size());
        assertThat(member.getAuthorities()).containsOnly("ROLE_USER");
    }

}
