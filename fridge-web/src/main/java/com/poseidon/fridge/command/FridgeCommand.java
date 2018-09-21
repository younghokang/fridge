package com.poseidon.fridge.command;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.poseidon.food.command.FoodCommand;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FridgeCommand {
    private Integer id;
    @NotNull
    @Size(min=2, max=15)
    private String nickname;
    private List<FoodCommand> foods;
    @NotNull
    private Long userId;
    
    @Builder
    public FridgeCommand(Integer id, String nickname, Long userId) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
    }

}
