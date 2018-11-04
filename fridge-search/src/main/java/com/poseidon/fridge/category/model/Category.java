package com.poseidon.fridge.category.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Category {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    @JsonIgnore
    @ElementCollection(fetch=FetchType.EAGER)
    @Column(name="keyword")
    private Set<String> keywords = new HashSet<>();
    
    @JsonIgnore
    @Transient
    private double score;
    
    @Builder
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
