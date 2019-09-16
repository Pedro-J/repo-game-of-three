package com.challenge.gameofthree.domain;

import com.challenge.gameofthree.event.GameEvent;
import com.challenge.gameofthree.resource.dto.GameStartDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

@Entity
@Table(name = "tb_game")
@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString(exclude = {"moves"})
public class GameEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_score")
    private int startScore;

    @Column(name = "creator")
    private int creator;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<MoveEntity> moves = new ArrayList<>();

    public GameEntity(GameStartDTO gameStartDTO) {
        this.startScore = gameStartDTO.getStartScore();
        this.creator = gameStartDTO.getPlayer();
    }

    public GameEntity addMove(MoveEntity move) {
        move.setGame(this);
        moves.add(move);
        return this;
    }

    public GameEntity loadMoves(Supplier<List<MoveEntity>> loader) {
        this.moves = loader.get();
        return this;
    }

    public GameEvent toEvent() {
        return getLastMove()
                .map(move -> new GameEvent(id, getOtherPlayer(move.getPlayer()), move.getFinalScore()))
                .orElse(new GameEvent(id, getOtherPlayer(creator), startScore));

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
        return getLastMove().filter( game -> game.getFinalScore() == 1).isPresent();
    }

    public String getGameStatus() {
        if ( getLastMove().isPresent() ) {
            MoveEntity lastMove = getLastMove().get();

            return new StringBuilder()
                    .append("M=performMove;")
                    .append("status=").append( verifyGameIsFinished() ? "GAME_FINISHED" : "ACTIVE").append(";")
                    .append("action=gameStatus;")
                    .append("game=").append(lastMove.getGame().id).append(";")
                    .append("player=").append(lastMove.getPlayer()).append(";")
                    .append("addedScore=").append(lastMove.getAddedScore()).append(";")
                    .append("originalScore=").append(lastMove.getOriginalScore()).append(";")
                    .append("finalScore=").append(lastMove.getFinalScore()).append(";")
                    .toString();
        }else {
            return new StringBuilder()
                    .append("M=performMove;")
                    .append("action=gameStatus;")
                    .append("status=NO_MOVE;")
                    .toString();
        }
    }

    public String getInitGameStatus() {
        return format("M=startGame;status=Initializing;gameId=%d,creator=%d;startScore=%d", id, creator, startScore);
    }
}
