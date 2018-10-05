package com.poseidon.member.model;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class MemberRequest {
    private Long id;
    @NotNull
    @Size(min=6, max=128)
    private String username;
    @NotNull
    @Size(max=72)
    private String password;
    private Set<String> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public Member toMember() {
        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .authorities(authorities.stream()
                        .map(role -> {
                            return new SimpleGrantedAuthority(role);
                        })
                        .collect(Collectors.toSet()))
                .accountNonExpired(accountNonExpired)
                .accountNonLocked(accountNonLocked)
                .credentialsNonExpired(credentialsNonExpired)
                .enabled(enabled)
                .build();
    }
    
    public void generateNewUser(PasswordEncoder passwordEncoder) {
        setAuthorities(AuthorityUtils.authorityListToSet(AuthorityUtils.createAuthorityList("ROLE_USER")));
        setPassword(passwordEncoder.encode(getPassword()));
        setAccountNonExpired(true);
        setAccountNonLocked(true);
        setCredentialsNonExpired(true);
        setEnabled(true);
    }
    
}
