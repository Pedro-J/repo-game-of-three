package com.challenge.gameofthreeplayer.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @NoArgsConstructor @AllArgsConstructor class GameStartDTO {

    @JsonProperty(value = "player")
    private Integer player;

    @JsonProperty(value = "start_score")
    private Integer startScore;
}
