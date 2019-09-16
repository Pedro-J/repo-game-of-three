package com.challenge.gameofthree.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


public @Data @NoArgsConstructor class GameMoveDTO {

    @NotNull
    @JsonProperty(value = "player")
    private Integer player;

    @NotNull
    @JsonProperty(value = "added_score")
    private Integer addedScore;

    @NotNull
    @JsonProperty(value = "original_score")
    private Integer originalScore;

    @NotNull
    @JsonProperty(value = "final_score")
    private Integer finalScore;
}
