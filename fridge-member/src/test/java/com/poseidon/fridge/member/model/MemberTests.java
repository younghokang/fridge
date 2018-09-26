package com.poseidon.fridge.member.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.poseidon.fridge.member.model.Member;

public class MemberTests {
    
    @Test
    public void newMemberConstructor() {
        Member member = new Member("user", "password");
        assertThat(member.toString()).isEqualTo("Member(id=" + member.getId()
            + ", username=" + member.getUsername()
            + ", password=" + member.getPassword() + ")");
    }

}
