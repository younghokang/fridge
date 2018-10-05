package com.poseidon.fridge.member.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.poseidon.fridge.member.model.Member;

public class MemberTests {
    
    @Test
    public void newMemberConstructor() {
        Member member = new Member("user", "password");
        assertThat(member.getUsername()).isEqualTo("user");
        assertThat(member.getPassword()).isEqualTo("password");
        assertThat(member.isAccountNonExpired()).isTrue();
        assertThat(member.isAccountNonLocked()).isTrue();
        assertThat(member.isCredentialsNonExpired()).isTrue();
        assertThat(member.isEnabled()).isTrue();
        assertThat(member.getAuthorities()).isEmpty();
    }

}
