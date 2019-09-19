package com.challenge.gameofthreeplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class GameOfThreePlayerApplication{

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(GameOfThreePlayerApplication.class, args);

		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		latch.await();
	}
}
