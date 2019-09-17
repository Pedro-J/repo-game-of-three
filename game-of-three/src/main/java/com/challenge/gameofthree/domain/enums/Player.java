package com.challenge.gameofthree.domain.enums;

import java.util.Arrays;

public enum Player {

    ONE(1),
    TWO(2);

    private Integer number;

    Player(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public static Player fromNumber(Integer number) {
        return Arrays.stream(values()).filter(player -> player.number.equals(number)).findFirst().get();
    }
}
