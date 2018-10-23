package com.poseidon.fridge.command;

import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.poseidon.food.command.Food;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Fridge {
    private Integer id;
    @NotNull
    @Size(min=2, max=15)
    private String nickname;
    private Collection<Food> foods;
    @NotNull
    private Long userId;
    
    @Builder
    public Fridge(Integer id, String nickname, Long userId) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
    }

}
