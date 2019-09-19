package com.challenge.gameofthreeplayer.util;

import com.challenge.gameofthreeplayer.GameOfThreePlayerApplication;
import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.dto.GameStartDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Bootstrap implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private GameApiClient gameApiClient;

    @Value("${game.maxscore}")
    private String maxScore;

    @Value("${game.player.number}")
    private String playerNumber;

    @Value("${game.keep.play}")
    private boolean keepPlay;

    @Override
    public void run(String... args) throws Exception {
        Integer maxScoreInt = Integer.valueOf(maxScore);
        Integer playerNumberInt = Integer.valueOf(playerNumber);
        initGame(playerNumberInt, maxScoreInt);
    }

    private void initGame(Integer number, Integer maxScore) throws InterruptedException {
        if ( keepPlay ) {
            do {
                LOGGER.info("-------------- NEW GAME ---------------");
                sendNewGameRequest(number,maxScore);
                Thread.sleep(20000);
            } while (keepPlay);
        }else {
            sendNewGameRequest(number,maxScore);
        }

    }

    private void sendNewGameRequest(Integer number, Integer maxScore) {
        Random generator = new Random();
        Integer generatedValue = generator.nextInt(maxScore);
        final Integer startScore = generatedValue < 100 ? 100 : generatedValue;
        ResponseEntity<?> response = gameApiClient.start(new GameStartDTO(number, startScore));

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.info("M=process;state=info;details=%s;message=Start Game send successfully to the server", response.getStatusCode());
        }
    }
}
