package com.challenge.gameofthree.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public @Data @AllArgsConstructor @NoArgsConstructor class GameEvent implements Serializable {

    @JsonProperty(value = "game_id")
    private Long gameId;

    @JsonProperty(value = "player")
    private Integer player;

    @JsonProperty(value = "current_score")
    private Integer currentScore;
}
