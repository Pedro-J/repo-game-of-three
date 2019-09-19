package com.challenge.gameofthree.domain;

import com.challenge.gameofthree.domain.enums.GameStatus;
import com.challenge.gameofthree.domain.enums.Player;
import com.challenge.gameofthree.event.GameEvent;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

@Entity
@Table(name = "tb_game")
@Data @AllArgsConstructor @Builder @ToString(exclude = {"moves"})
public class GameEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_score")
    private Integer startScore;

    @Column(name = "creator")
    @Enumerated(value = EnumType.STRING)
    private Player creator;

    @Column(name = "ack_player_1")
    private Boolean ackPlayer1;

    @Column(name = "ack_player_2")
    private Boolean ackPlayer2;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private GameStatus status;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<MoveEntity> moves = new ArrayList<>();

    public GameEntity() {
        ackPlayer1 = false;
        ackPlayer2 = false;
        status = GameStatus.NEW;
    }

    public GameEntity startGame(GameStartDTO gameStartDTO) {

        if( isPlayerOne(gameStartDTO.getPlayer()) ) {
            startScore = gameStartDTO.getStartScore();
        }

        if( status == GameStatus.NEW ) {
            creator = Player.fromNumber(gameStartDTO.getPlayer());
        }

        setACK(gameStartDTO.getPlayer());
        setGameStarted();
        return this;
    }

    private void setACK(Integer playerNumber) {
        if ( isPlayerOne(playerNumber) ) {
            ackPlayer1 = true;
            status = GameStatus.WAITING_PLAYER_2;
        }else {
            ackPlayer2 = true;
            status = GameStatus.WAITING_PLAYER_1;
        }
    }

    private void setGameStarted() {
        if ( ackPlayer1 && ackPlayer2 ) {
            status = GameStatus.STARTED;
        }
    }

    public boolean isGameStarted() {
        return status == GameStatus.STARTED;
    }

    private boolean isPlayerOne(Integer playerNumber) {
        return Player.fromNumber(playerNumber).equals(Player.ONE);
    }

    public void addMove(MoveEntity move) {
        move.setGame(this);
        moves.add(move);
    }

    public GameEntity loadMoves(Supplier<List<MoveEntity>> loader) {
        this.moves = loader.get();
        return this;
    }

    public GameEvent toEvent() {
        return getLastMove()
                .map(move -> new GameEvent(id, getOtherPlayer(move.getPlayer().getNumber()), move.getFinalScore()))
                .orElse(new GameEvent(id, Player.TWO.getNumber(), startScore));

    }

    public Optional<MoveEntity> getLastMove() {
        return moves != null && !moves.isEmpty() ? Optional.of(moves.get(moves.size() - 1)) : Optional.empty();
    }

    public void addMoves(List<MoveEntity> moves) {
        moves.forEach(this::addMove);
    }

    public Integer getOtherPlayer(Integer number) {
        return (number % 2 == 0 ? 1 : 2);
    }

    public boolean verifyGameIsNotFinished() {
        return !verifyGameIsFinished();
    }

    public boolean verifyGameIsFinished() {
        return getLastMove().filter( game -> game.getFinalScore() < 3).isPresent();
    }

    public boolean hasWinner() {
        return getLastMove().filter( game -> game.getFinalScore() == 1).isPresent();
    }

    public void changeToFinished() {
        status = GameStatus.FINISHED;
    }

    public void printGameStatus(Logger logger) {
        if ( getLastMove().isPresent() ) {
            MoveEntity lastMove = getLastMove().get();

            logger.info(new StringBuilder()
                    .append("Game=").append(lastMove.getGame().id).append(";")
                    .append("Player_").append(lastMove.getPlayer().name())
                    .append(verifyGameIsFinished() ? hasWinner() ? " IS WINNER!!!": " FINISHES WITH "+ lastMove.getFinalScore() +", THERE NO WINNER" : "").append(";")
                    .append("Status=").append( verifyGameIsFinished() ? "GAME_FINISHED" : "ACTIVE").append(";")
                    .append("AddedScore=").append(lastMove.getAddedScore()).append(";")
                    .append("OriginalScore=").append(lastMove.getOriginalScore()).append(";")
                    .append("FinalScore=").append(lastMove.getFinalScore()).append(";\n")
                    .toString());
        }else {
            logger.info(new StringBuilder()
                    .append("action=gameStatus;")
                    .append("status=NO_MOVE;")
                    .toString());
        }
    }

    public void printInitGameStatus(Logger logger) {
        logger.info(" ---------------- NEW GAME ------------------- \n");
        logger.info(format("Status=Initializing;Game=%d;Creator=%d;StartScore=%d; \n", id, creator.getNumber(), startScore));
    }
}
