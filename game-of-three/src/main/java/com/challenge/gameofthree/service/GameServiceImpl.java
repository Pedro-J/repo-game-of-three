package com.challenge.gameofthree.service;

import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import com.challenge.gameofthree.event.publisher.impl.GameEventPublisher;
import com.challenge.gameofthree.repository.GameRepository;
import com.challenge.gameofthree.resource.dto.GameMoveDTO;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.util.Optional.of;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

    private GameRepository gameRepository;
    private GameEventPublisher gameEventPublisher;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, GameEventPublisher gameEventPublisher) {
        this.gameRepository = gameRepository;
        this.gameEventPublisher = gameEventPublisher;
    }

    @Override
    public void performMove(final long gameId, final GameMoveDTO move) {
        gameRepository.findByIdWithMoves(gameId)
                .map( game -> {
                    gameRepository.save(game.addMove(new MoveEntity(move)));
                    LOGGER.info(game.getGameStatus());
                    return game;
                })
                .filter(GameEntity::verifyGameIsNotFinished)
                .ifPresent(game -> gameEventPublisher.publish(game.toEvent()));
    }

    @Override
    public void startGame(GameStartDTO gameStartDTO) {
        LOGGER.info("\n ---------------- NEW GAME ------------------- \n");
        of(gameRepository.save(new GameEntity(gameStartDTO)))
            .ifPresent( game -> {
                LOGGER.info(game.getInitGameStatus());
                gameEventPublisher.publish(game.toEvent());
            });
    }




}
