package com.challenge.gameofthree;

import com.challenge.gameofthree.util.Bootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameOfThreeApplication implements CommandLineRunner {

	private Bootstrap bootstrap;

	@Autowired
	public GameOfThreeApplication(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	@Override
	public void run(String... args) throws Exception {
		bootstrap.init();
	}

	public static void main(String[] args) {
		SpringApplication.run(GameOfThreeApplication.class, args);
	}
}
