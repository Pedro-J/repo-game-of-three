package com.challenge.gameofthreeplayer.client;

import com.challenge.gameofthreeplayer.dto.GameMoveDTO;
import com.challenge.gameofthreeplayer.dto.GameStartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Component
public class GameApiClient {

    private RestTemplate restTemplate;

    @Value("${game.api.baseurl}")
    private String baseUrl;

    @Value("${game.api.resource.move.uri}")
    private String gameMoveUri;

    @Value("${game.api.resource.start.uri}")
    private String gameStartUri;

    @Autowired
    public GameApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> doMove(Long gameId, GameMoveDTO gameMoveDTO) {
        HttpEntity<GameMoveDTO> requestEntity = new HttpEntity<>(gameMoveDTO, createHeader());
        return restTemplate.exchange(baseUrl + String.format(gameMoveUri, gameId), HttpMethod.POST, requestEntity, String.class);
    }

    public ResponseEntity<?> start(GameStartDTO gameStartDTO) {
        HttpEntity<GameStartDTO> requestEntity = new HttpEntity<>(gameStartDTO, createHeader());
        return restTemplate.exchange(baseUrl + gameStartUri, HttpMethod.POST, requestEntity, String.class);
    }

    private HttpHeaders createHeader()  {
        final HttpHeaders header = new HttpHeaders();
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return header;
    }
}
