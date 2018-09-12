package com.poseidon.fridge.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FridgeCommand {
    private Integer id;
    @NotNull
    @Size(min=2, max=15)
    private String nickname;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
