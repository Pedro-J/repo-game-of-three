package com.challenge.gameofthree.util;


import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import com.challenge.gameofthree.repository.GameRepository;
import com.challenge.gameofthree.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@Component
public class Bootstrap {

    private GameRepository gameRepository;
    private MoveRepository moveRepository;

    @Autowired
    public Bootstrap(GameRepository gameRepository, MoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
    }

    @Transactional
    public void init() {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setCreator(1);
        gameEntity.setStartScore(1000);

        MoveEntity move1 = MoveEntity.builder().player(2).originalScore(100).addedScore(-1).finalScore(33).build();
        MoveEntity move2 = MoveEntity.builder().player(1).originalScore(33).addedScore(0).finalScore(11).build();
        MoveEntity move3 = MoveEntity.builder().player(2).originalScore(11).addedScore(1).finalScore(4).build();
        MoveEntity move4 = MoveEntity.builder().player(1).originalScore(4).addedScore(-1).finalScore(1).build();

        gameEntity.addMoves(of(move1, move2, move3, move4).collect(toList()));

    }
}
