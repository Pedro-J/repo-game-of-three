package com.challenge.gameofthree.repository;

import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.challenge.gameofthree.domain.enums.Player.ONE;
import static com.challenge.gameofthree.domain.enums.Player.TWO;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    private GameEntity gameEntity = null;

    @After
    public void after() {
        clean();
    }

    @Test
    public void testFindCurrentGame(){
        saveOneGame();

        Optional<GameEntity> resultGame = gameRepository.findCurrentGame();

        assertTrue(resultGame.isPresent());
        assertThat(resultGame.get().getCreator(), equalTo(gameEntity.getCreator()));
        assertThat(resultGame.get().getStartScore(), equalTo(gameEntity.getStartScore()));
        assertThat(resultGame.get().getMoves(), hasSize(2));
    }

    @Test
    public void testFindCurrentGameWhenThereNoGame() {
        Optional<GameEntity> resultGame = gameRepository.findCurrentGame();
        assertFalse(resultGame.isPresent());
    }

    @Test
    public void testFindByIdWithMoves() {
        saveOneGame();
        Optional<GameEntity> resultGame = gameRepository.findByIdWithMoves(1L);

        assertTrue(resultGame.isPresent());
        assertThat(resultGame.get().getMoves(), hasSize(2));
    }

    @Test
    public void testFindByIdWhenIdDoesNotExistInDatabase() {
        saveOneGame();
        Optional<GameEntity> resultGame = gameRepository.findByIdWithMoves(2000L);

        assertFalse(resultGame.isPresent());
    }


    private void saveOneGame() {
        gameEntity = new GameEntity();
        gameEntity.setCreator(ONE);
        gameEntity.setStartScore(1000);

        MoveEntity move1 = MoveEntity.builder().player(TWO).originalScore(100).addedScore(-1).finalScore(33).build();
        MoveEntity move2 = MoveEntity.builder().player(ONE).originalScore(33).addedScore(0).finalScore(11).build();
        gameEntity.addMoves(of(move1, move2).collect(toList()));

        entityManager.persist(gameEntity);
    }

    private void clean() {
        if ( gameEntity != null ) {

            GameEntity savedGame = entityManager.find(GameEntity.class, gameEntity.getId());

            if (savedGame != null) {
                entityManager.remove(savedGame);
            }
        }
    }
}
