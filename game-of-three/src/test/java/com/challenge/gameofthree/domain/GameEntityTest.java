package com.challenge.gameofthree.domain;

import com.challenge.gameofthree.domain.enums.GameStatus;
import com.challenge.gameofthree.domain.enums.Player;
import com.challenge.gameofthree.event.GameEvent;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import com.challenge.gameofthree.util.Bootstrap;
import org.junit.Test;

import static com.challenge.gameofthree.domain.enums.Player.TWO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameEntityTest {

    @Test
    public void testInitDefaultValuesAreAsExpected() {
        final GameEntity newGame = new GameEntity();
        assertFalse(newGame.getAckPlayer1());
        assertFalse(newGame.getAckPlayer2());
        assertEquals(GameStatus.NEW, newGame.getStatus());
    }

    @Test
    public void testStartGame() {
        final GameEntity game = new GameEntity();
        final GameStartDTO gameStartDTO = new GameStartDTO(1, 1000);

        game.startGame(gameStartDTO);

        assertTrue(game.getAckPlayer1());
        assertFalse(game.getAckPlayer2());
        assertEquals(Player.ONE, game.getCreator());
        assertEquals(GameStatus.WAITING_PLAYER_2, game.getStatus());

        final GameStartDTO gameStartDTO2 = new GameStartDTO(2, 50);
        game.startGame(gameStartDTO2);

        assertTrue(game.getAckPlayer1());
        assertTrue(game.getAckPlayer2());
        assertEquals(Player.ONE, game.getCreator());
        assertEquals(GameStatus.STARTED, game.getStatus());
        assertEquals(game.getStartScore(), gameStartDTO.getStartScore());
    }

    @Test
    public void testIsGameStarted() {
        final GameEntity game = new GameEntity();

        assertFalse(game.isGameStarted());

        final GameStartDTO gameStartDTO = new GameStartDTO(1, 1000);

        game.startGame(gameStartDTO);

        assertFalse(game.isGameStarted());

        final GameStartDTO gameStartDTO2 = new GameStartDTO(2, 50);
        game.startGame(gameStartDTO2);

        assertTrue(game.isGameStarted());
    }

    @Test
    public void testToEvent() {
        GameEntity game = Bootstrap.buildSimpleGameEntity();
        GameEvent event = game.toEvent();

        assertEquals(TWO.getNumber(), event.getPlayer());
        assertEquals(game.getLastMove().get().getFinalScore(), event.getCurrentScore());
        assertEquals(game.getId(), event.getGameId());
    }

    @Test
    public void testVerifyGameIsFinished() {
        assertTrue(Bootstrap.buildSimpleGameEntity().verifyGameIsFinished());
        assertFalse(new GameEntity().verifyGameIsFinished());
    }
}
