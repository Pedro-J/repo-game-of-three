package com.challenge.gameofthree.resource;


import com.challenge.gameofthree.resource.dto.GameMoveDTO;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import com.challenge.gameofthree.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/gameofthree/games")
public class GameResource {

    private GameService gameService;

    @Autowired
    public GameResource(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public void startNewGame(@Valid @RequestBody GameStartDTO gameStartDTO) {
        gameService.startGame(gameStartDTO);
    }

    @PostMapping(value = "/{id}/move", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public void performNewMove(@PathVariable Integer id, @Valid @RequestBody GameMoveDTO gameMoveDTO) {
        gameService.performMove(id, gameMoveDTO);
    }
}
