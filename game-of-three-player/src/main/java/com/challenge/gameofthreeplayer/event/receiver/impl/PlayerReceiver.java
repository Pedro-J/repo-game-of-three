package com.challenge.gameofthreeplayer.event.receiver.impl;

import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.dto.GameMoveDTO;
import com.challenge.gameofthreeplayer.dto.GameStartDTO;
import com.challenge.gameofthreeplayer.event.GameEvent;
import com.challenge.gameofthreeplayer.event.receiver.JSONReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Random;
import java.util.stream.IntStream;

public class PlayerReceiver extends JSONReceiver<GameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerReceiver.class);

    private GameApiClient gameApiClient;

    public PlayerReceiver(GameApiClient gameApiClient, Integer number, Integer maxScore) {
        super(number, GameEvent.class);
        this.gameApiClient = gameApiClient;
        LOGGER.info("M=init;receiver="+ getName());
        initGame(number, maxScore);
    }

    @Override
    protected void process(GameEvent event) {
        GameMoveDTO gameMoveDTO = performMove(event.getCurrentScore());
        gameMoveDTO.setPlayer(event.getPlayer());
        ResponseEntity<?> response = gameApiClient.doMove(event.getGameId(), gameMoveDTO);

        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("M=process;state=info;details=%s;message=Move request send successfully to the server", response.getStatusCode());
        }
    }

    private GameMoveDTO performMove(Integer originalScore) {
        Integer addedScore = IntStream.of(-1, 0, 1).filter(value -> (originalScore + value) % 3 == 0 )
                .findFirst().orElse(-1);

        Integer finalScore = (originalScore + addedScore) / 3;

        GameMoveDTO gameMoveDTO = new GameMoveDTO();
        gameMoveDTO.setAddedScore(addedScore);
        gameMoveDTO.setFinalScore(finalScore);
        gameMoveDTO.setOriginalScore(originalScore);
        return gameMoveDTO;
    }

    private void initGame(Integer number, Integer maxScore) {
        Random generator = new Random();
        Integer startScore = generator.nextInt(maxScore);
        if ( number == 1 ){
            ResponseEntity<?> response = gameApiClient.start(new GameStartDTO(number, startScore));

            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("M=process;state=info;details=%s;message=Start Game send successfully to the server", response.getStatusCode());
            }
        }
    }


}
