package com.challenge.gameofthree.resource;


import com.challenge.gameofthree.exception.handler.RestResponseEntityExceptionHandler;
import com.challenge.gameofthree.resource.dto.GameMoveDTO;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import com.challenge.gameofthree.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {GameResource.class, RestResponseEntityExceptionHandler.class})
public class GameResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private final String baseUri = "/gameofthree/games";
    private final String moveUri = baseUri + "/%d/move";
    private final Integer gameId = 1;

    private final String moveRequestBody = "{\"player\": 1, \"added_score\": 0, \"original_score\": 30, \"final_score\": 10}";
    private final String startRequestBody = "{\"player\": 1, \"start_score\": 10000}";

    @Test
    public void shouldStartGameAndReturnResponseCode200() throws Exception {

        doNothing().when(gameService).startGame(any(GameStartDTO.class));

        mockMvc.perform(post(baseUri)
                .content(startRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotStartGameAndReturnResponseCode500WhenExpectedErrorOccurs() throws Exception {

        doThrow(RuntimeException.class).when(gameService).startGame(any(GameStartDTO.class));

        mockMvc.perform(post(baseUri)
                .content(startRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldNotStartGameAndReturnResponseCode400WhenInvalidRequestBodyIsSent() throws Exception {

        final String invalidRequestBody = "{\"player\": , \"start_score\": null }";

        doNothing().when(gameService).startGame(any(GameStartDTO.class));

        mockMvc.perform(post(baseUri)
                .content(invalidRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldNotStartGameAndReturnResponseCode400WhenEmptyValuesToScoreAndPlayerIsSent() throws Exception {

        final String invalidRequestBody = "{\"player\": \"\", \"start_score\": \"\"}";

        doNothing().when(gameService).startGame(any(GameStartDTO.class));

        mockMvc.perform(post(baseUri)
                .content(invalidRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem("must not be null")));
    }


    @Test
    public void shouldMoveGameAndReturnResponseCode200() throws Exception {

        doNothing().when(gameService).performMove(anyLong(), any(GameMoveDTO.class));

        mockMvc.perform(post(String.format(moveUri, gameId))
                .content(moveRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotMoveGameAndReturnResponseCode500WhenExpectedErrorOccurs() throws Exception {

        doThrow(RuntimeException.class).when(gameService).performMove(anyLong(), any(GameMoveDTO.class));

        mockMvc.perform(post(String.format(moveUri, gameId))
                .content(moveRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldNotMoveGameAndReturnResponseCode400WhenInvalidRequestBodyIsSent() throws Exception {

        String moveRequestBody = "{\"playe\": 1, \"ad_score\": 0, \"original_sce\": 30, \"final_score\": 10}";

        doThrow(RuntimeException.class).when(gameService).performMove(anyLong(), any(GameMoveDTO.class));

        mockMvc.perform(post(String.format(moveUri, gameId))
                .content(moveRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotMoveGameAndReturnResponseCode400WhenInvalidJsonFormatIsSent() throws Exception {

        String moveRequestBody = "\"playe\": 1, \"ad_score\" 0 \"original_sce\": 30, \"final_score\": 10}";

        doThrow(RuntimeException.class).when(gameService).performMove(anyLong(), any(GameMoveDTO.class));

        mockMvc.perform(post(String.format(moveUri, gameId))
                .content(moveRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotMoveGameAndReturnResponseCode400WhenAllEmptyFieldsOnPayloadIsSent() throws Exception {

        String moveRequestBody = "{\"player\": \"\", \"added_score\": \"\", \"original_sce\": \"\", \"final_score\": \"\"}";

        doThrow(RuntimeException.class).when(gameService).performMove(anyLong(), any(GameMoveDTO.class));

        mockMvc.perform(post(String.format(moveUri, gameId))
                .content(moveRequestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("must not be null")));
    }

}
