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
import java.util.Optional;

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
                .map( game -> updateGameInfo(game, move))
                .filter(GameEntity::verifyGameIsNotFinished)
                .ifPresent(game -> gameEventPublisher.publish(game.toEvent()));
    }

    @Override
    public void startGame(GameStartDTO gameStartDTO) {

        Optional<GameEntity> currentGame = gameRepository.findCurrentGame();

        if ( currentGame.isPresent() ) {
            currentGame.map(game -> game.startGame(gameStartDTO))
                    .filter(GameEntity::isGameStarted)
                    .map(game -> gameRepository.save(game))
                    .ifPresent(game -> {
                        game.printInitGameStatus(LOGGER);
                        gameEventPublisher.publish(game.toEvent());
                    });
        }else {
            gameRepository.save(new GameEntity().startGame(gameStartDTO));
        }
    }

    private GameEntity updateGameInfo(final GameEntity game, final GameMoveDTO move) {
        if (game.verifyGameIsFinished()){
            game.changeToFinished();
        }
        game.addMove(new MoveEntity(move));

        game.printGameStatus(LOGGER);
        return gameRepository.save(game);
    }
}
