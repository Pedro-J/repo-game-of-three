package com.challenge.gameofthree.util;


import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import com.challenge.gameofthree.repository.GameRepository;
import com.challenge.gameofthree.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import static com.challenge.gameofthree.domain.enums.Player.ONE;
import static com.challenge.gameofthree.domain.enums.Player.TWO;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@Component
@Transactional
public class Bootstrap implements CommandLineRunner {

    private GameRepository gameRepository;
    private MoveRepository moveRepository;

    @Autowired
    public Bootstrap(GameRepository gameRepository, MoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        gameRepository.save(buildSimpleGameEntity());
    }

    public static GameEntity buildSimpleGameEntity() {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setCreator(ONE);
        gameEntity.setStartScore(1000);
        gameEntity.setAckPlayer1(true);
        gameEntity.setAckPlayer2(true);

        MoveEntity move1 = MoveEntity.builder().player(TWO).originalScore(100).addedScore(-1).finalScore(33).build();
        MoveEntity move2 = MoveEntity.builder().player(ONE).originalScore(33).addedScore(0).finalScore(11).build();
        MoveEntity move3 = MoveEntity.builder().player(TWO).originalScore(11).addedScore(1).finalScore(4).build();
        MoveEntity move4 = MoveEntity.builder().player(ONE).originalScore(4).addedScore(-1).finalScore(1).build();
        gameEntity.changeToFinished();

        gameEntity.addMoves(of(move1, move2, move3, move4).collect(toList()));
        return gameEntity;
    }
}
