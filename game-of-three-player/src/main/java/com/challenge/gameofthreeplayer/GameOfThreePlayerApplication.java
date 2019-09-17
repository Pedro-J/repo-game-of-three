package com.challenge.gameofthreeplayer;

import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.dto.GameStartDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class GameOfThreePlayerApplication implements CommandLineRunner{

	@Value("${game.maxscore}")
	private String maxScore;

	@Value("${game.player.number}")
	private String playerNumber;

	@Value("${game.start}")
	private boolean start;

	@Autowired
	private GameApiClient gameApiClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(GameOfThreePlayerApplication.class);

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(GameOfThreePlayerApplication.class, args);

		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		latch.await();
	}

	@Override
	public void run(String... args) throws Exception {
		if( start ) {
			Integer maxScoreInt = Integer.valueOf(maxScore);
			Integer playerNumberInt = Integer.valueOf(playerNumber);
			initGame(playerNumberInt, maxScoreInt);
		}
	}

	private void initGame(Integer number, Integer maxScore) throws InterruptedException {
		Random generator = new Random();
		Integer startScore = generator.nextInt(maxScore);
		ResponseEntity<?> response = gameApiClient.start(new GameStartDTO(number, startScore));

		if (response.getStatusCode() == HttpStatus.CREATED) {
			LOGGER.info("M=process;state=info;details=%s;message=Start Game send successfully to the server", response.getStatusCode());
		}
	}

}
