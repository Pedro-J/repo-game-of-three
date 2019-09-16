package com.challenge.gameofthree.resource.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public @Data @NoArgsConstructor class GameStartDTO {

    @NotNull
    @JsonProperty(value = "player")
    private Integer player;

    @NotNull
    @JsonProperty(value = "start_score")
    private Integer startScore;
}
