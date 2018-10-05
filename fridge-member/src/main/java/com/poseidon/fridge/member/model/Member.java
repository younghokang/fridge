package com.poseidon.fridge.member.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NaturalId
    @Column(unique=true)
    private String username;
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    
    @ElementCollection
    @Column(name="authority")
    private Set<String> authorities = new HashSet<>();

    public Member(String username, String password) {
        this(username, password, null);
    }
    
    public Member(String username, String password, Set<String> authorities) {
        this.username = username;
        this.password = password;
        if(authorities != null) {
            this.authorities.addAll(authorities);
        }
    }
    
}
