package com.challenge.gameofthreeplayer.event.receiver.impl;

import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.dto.GameMoveDTO;
import com.challenge.gameofthreeplayer.event.GameEvent;
import com.challenge.gameofthreeplayer.event.receiver.JSONReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Scanner;
import java.util.stream.IntStream;

import static java.lang.String.format;

public class PlayerReceiver extends JSONReceiver<GameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerReceiver.class);

    private GameApiClient gameApiClient;
    private boolean auto;

    public PlayerReceiver(GameApiClient gameApiClient, Integer number, boolean auto) {
        super(number, GameEvent.class);
        this.gameApiClient = gameApiClient;
        this.auto = auto;
        LOGGER.info("M=init;receiver="+ getName());
    }

    @Override
    protected void process(GameEvent event) {
        GameMoveDTO move = performMove(event.getCurrentScore());
        move.setPlayer(event.getPlayer());

        printStatus(move, event);
        sendNewMove(event.getGameId(), move);
    }

    private void sendNewMove(Long gameId, GameMoveDTO move) {
        try {
            ResponseEntity<?> response = gameApiClient.doMove(gameId, move);

            if (response.getStatusCode() == HttpStatus.OK) {
                LOGGER.info(format("M=process;state=info;message=Move request send successfully to the server;details=%s;", response.getStatusCode().toString()));
            }
        }catch(Exception ex) {
            LOGGER.error("M=process;state=error;message=Move cloud not be sent;cause=%s;", ex.getMessage());
        }
    }

    private void printStatus(GameMoveDTO move, GameEvent event) {
        LOGGER.info(format("GAME_INFO - Game: %d - Player_%d;current_score:%d;added_score:%s;final_score:%d;",
                event.getGameId(), move.getPlayer(), move.getOriginalScore(), move.getAddedScore(), move.getFinalScore()));

        if (move.getFinalScore() == 1) {
            LOGGER.info("YOU WIN!! CONGRATS!!");
        }else if ( move.getFinalScore() < 3 ) {
            LOGGER.info("THIS GAME HAS NO WINNER.");
        }
    }

    private GameMoveDTO performMove(Integer originalScore) {
        Integer addedScore;

        if( auto ) {
            addedScore = getAutoAddedScore(originalScore);
        }else{
            addedScore = getManualAddedScore(originalScore);
        }

        Integer finalScore = (originalScore + addedScore) / 3;

        return new GameMoveDTO(addedScore, originalScore, finalScore);
    }

    private Integer getAutoAddedScore(Integer originalScore) {
        return IntStream.of(-1, 0, 1).filter(value -> (originalScore + value) % 3 == 0 )
                .findFirst().orElse(-1);
    }

    private Integer getManualAddedScore(Integer originalScore) {
        Integer addedValue;

        do {
            Scanner keyboard = new Scanner(System.in);
            System.out.println(String.format("Add a score of 1, 0 or -1 to the (current score is %d): ", originalScore));
            addedValue = keyboard.nextInt();

        }while ( addedValue > 1 || addedValue < -1 );

        return addedValue;
    }

}
