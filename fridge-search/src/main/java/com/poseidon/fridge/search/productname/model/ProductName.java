package com.poseidon.fridge.search.productname.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class ProductName {
    @Id
    private String name;
    private long score;
    
    @Builder
    public ProductName(String name, long score) {
        this.name = name;
        this.score = score;
    }

}
