package com.challenge.gameofthreeplayer.event.receiver.impl;

import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.dto.GameMoveDTO;
import com.challenge.gameofthreeplayer.event.GameEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

public class PlayerReceiverTest {

    @Mock
    private GameApiClient gameApiClient;

    private PlayerReceiver playerReceiver;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        playerReceiver = new PlayerReceiver(gameApiClient, 1, true);
    }

    @Test
    public void testProcessMessageAuto() {
        GameEvent gameEvent = new GameEvent(1L, 1, 100);

        ArgumentCaptor<GameMoveDTO> captorMoveDTO = ArgumentCaptor.forClass(GameMoveDTO.class);
        ArgumentCaptor<Long> captorGameId = ArgumentCaptor.forClass(Long.class);

        when(gameApiClient.doMove(anyLong(), any(GameMoveDTO.class))).thenReturn(ok().build());

        playerReceiver.process(gameEvent);

        verify(gameApiClient).doMove(captorGameId.capture(), captorMoveDTO.capture());

        assertEquals(captorGameId.getValue(), gameEvent.getGameId());
        assertEquals( gameEvent.getPlayer(), captorMoveDTO.getValue().getPlayer());
        assertEquals(-1, captorMoveDTO.getValue().getAddedScore().intValue());
        assertEquals(gameEvent.getCurrentScore(), captorMoveDTO.getValue().getOriginalScore());
        assertEquals(((gameEvent.getCurrentScore() - 1) / 3), captorMoveDTO.getValue().getFinalScore().intValue());
    }

    @Test
    public void testProcessMessageWhenClientThrowsException() {
        GameEvent gameEvent = new GameEvent(1L, 1, 100);

        ArgumentCaptor<GameMoveDTO> captorMoveDTO = ArgumentCaptor.forClass(GameMoveDTO.class);
        ArgumentCaptor<Long> captorGameId = ArgumentCaptor.forClass(Long.class);

        when(gameApiClient.doMove(anyLong(), any(GameMoveDTO.class))).thenThrow(RuntimeException.class);

        playerReceiver.process(gameEvent);

        verify(gameApiClient).doMove(captorGameId.capture(), captorMoveDTO.capture());

        assertEquals(captorGameId.getValue(), gameEvent.getGameId());
        assertEquals( gameEvent.getPlayer(), captorMoveDTO.getValue().getPlayer());
        assertEquals(-1, captorMoveDTO.getValue().getAddedScore().intValue());
        assertEquals(gameEvent.getCurrentScore(), captorMoveDTO.getValue().getOriginalScore());
        assertEquals(((gameEvent.getCurrentScore() - 1) / 3), captorMoveDTO.getValue().getFinalScore().intValue());
    }

    @Test
    public void testProcessMessageWhenClientCallNotReturns2xxResponse() {
        GameEvent gameEvent = new GameEvent(1L, 1, 100);

        ArgumentCaptor<GameMoveDTO> captorMoveDTO = ArgumentCaptor.forClass(GameMoveDTO.class);
        ArgumentCaptor<Long> captorGameId = ArgumentCaptor.forClass(Long.class);

        when(gameApiClient.doMove(anyLong(), any(GameMoveDTO.class))).thenReturn(badRequest().build());

        playerReceiver.process(gameEvent);

        verify(gameApiClient).doMove(captorGameId.capture(), captorMoveDTO.capture());

        assertEquals(captorGameId.getValue(), gameEvent.getGameId());
        assertEquals( gameEvent.getPlayer(), captorMoveDTO.getValue().getPlayer());
        assertEquals(-1, captorMoveDTO.getValue().getAddedScore().intValue());
        assertEquals(gameEvent.getCurrentScore(), captorMoveDTO.getValue().getOriginalScore());
        assertEquals(((gameEvent.getCurrentScore() - 1) / 3), captorMoveDTO.getValue().getFinalScore().intValue());
    }
}
