package com.challenge.gameofthree.service;

import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import com.challenge.gameofthree.event.GameEvent;
import com.challenge.gameofthree.event.publisher.impl.GameEventPublisher;
import com.challenge.gameofthree.repository.GameRepository;
import com.challenge.gameofthree.resource.dto.GameMoveDTO;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static com.challenge.gameofthree.domain.enums.Player.ONE;
import static com.challenge.gameofthree.domain.enums.Player.TWO;
import static com.challenge.gameofthree.util.Bootstrap.buildSimpleGameEntity;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameEventPublisher gameEventPublisher;

    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(gameRepository, gameEventPublisher);
    }

    @Test
    public void testStartGameOnePlayerRequested(){
        GameStartDTO gameStartDTO = new GameStartDTO(1, 5000);
        GameEntity gameEntity = buildSimpleGameEntity();

        when(gameRepository.findCurrentGame()).thenReturn(empty());
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);

        gameService.startGame(gameStartDTO);

        verify(gameRepository).findCurrentGame();
        verify(gameRepository).save(any(GameEntity.class));
        verify(gameEventPublisher, never()).publish(any(GameEvent.class));
    }

    @Test
    public void testStartGameTwoPlayersRequested(){
        GameStartDTO gameStartDTO = new GameStartDTO(1, 5000);
        GameStartDTO gameStartDTO2 = new GameStartDTO(2, 100);
        GameEntity gameEntity = buildSimpleGameEntity();
        gameEntity.startGame(gameStartDTO);

        when(gameRepository.findCurrentGame()).thenReturn(of(gameEntity));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);

        gameService.startGame(gameStartDTO2);

        verify(gameRepository).findCurrentGame();
        verify(gameRepository).save(any(GameEntity.class));
        verify(gameEventPublisher).publish(any(GameEvent.class));
    }

    @Test
    public void testPerformMoveWhenGameIsStarted() {
        Long gameId = 1L;
        GameMoveDTO gameMoveDTO = new GameMoveDTO(1, -1, 50, 49);
        GameEntity gameEntity = new GameEntity();

        GameStartDTO gameStartDTO = new GameStartDTO(1, 5000);
        GameStartDTO gameStartDTO2 = new GameStartDTO(2, 100);

        gameEntity.startGame(gameStartDTO);
        gameEntity.startGame(gameStartDTO2);

        when(gameRepository.findByIdWithMoves(gameId)).thenReturn(of(gameEntity));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);

        gameService.performMove(gameId, gameMoveDTO);

        verify(gameEventPublisher).publish(any(GameEvent.class));
    }

    @Test
    public void testPerformMoveWheGameIsFinished() {
        long gameId = 1L;
        GameMoveDTO gameMoveDTO = new GameMoveDTO(1, -1, 4, 1);

        GameEntity gameEntity = new GameEntity();
        gameEntity.setCreator(ONE);
        gameEntity.setStartScore(100);
        gameEntity.setAckPlayer1(true);
        gameEntity.setAckPlayer2(true);

        MoveEntity move1 = MoveEntity.builder().player(TWO).originalScore(100).addedScore(-1).finalScore(33).build();
        MoveEntity move2 = MoveEntity.builder().player(ONE).originalScore(33).addedScore(0).finalScore(11).build();
        MoveEntity move3 = MoveEntity.builder().player(TWO).originalScore(11).addedScore(1).finalScore(4).build();

        gameEntity.addMoves(Stream.of(move1, move2, move3).collect(toList()));

        when(gameRepository.findByIdWithMoves(gameId)).thenReturn(of(gameEntity));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);

        gameService.performMove(gameId, gameMoveDTO);

        verify(gameEventPublisher, never()).publish(any(GameEvent.class));
    }
}
