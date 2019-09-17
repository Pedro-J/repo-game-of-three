package com.challenge.gameofthreeplayer.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


public @Data @NoArgsConstructor class GameMoveDTO {

    @JsonProperty(value = "player")
    private Integer player;

    @JsonProperty(value = "added_score")
    private Integer addedScore;

    @JsonProperty(value = "original_score")
    private Integer originalScore;

    @JsonProperty(value = "final_score")
    private Integer finalScore;

    public GameMoveDTO(Integer addedScore, Integer originalScore, Integer finalScore) {
        this.addedScore = addedScore;
        this.finalScore = finalScore;
        this.originalScore = originalScore;
    }
}
