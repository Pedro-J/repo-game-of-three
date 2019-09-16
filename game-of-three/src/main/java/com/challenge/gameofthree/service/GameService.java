package com.challenge.gameofthree.service;

import com.challenge.gameofthree.resource.dto.GameMoveDTO;
import com.challenge.gameofthree.resource.dto.GameStartDTO;

public interface GameService {
    void performMove(long gameId, GameMoveDTO gameMoveDTO);
    void startGame(GameStartDTO gameStartDTO);
}
