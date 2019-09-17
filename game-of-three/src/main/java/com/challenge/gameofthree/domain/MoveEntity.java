package com.challenge.gameofthree.domain;

import com.challenge.gameofthree.domain.enums.Player;
import com.challenge.gameofthree.resource.dto.GameMoveDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "tb_move")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public  class MoveEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @Column(name = "player")
    @Enumerated(value = EnumType.STRING)
    private Player player;

    @Column(name = "added_score")
    private Integer addedScore;

    @Column(name = "original_score")
    private Integer originalScore;

    @Column(name = "final_score")
    private Integer finalScore;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public MoveEntity(GameMoveDTO move) {
        setMove(move);
    }

    public void setMove(GameMoveDTO move) {
        this.setPlayer(Player.fromNumber(move.getPlayer()));
        this.setAddedScore(move.getAddedScore());
        this.setOriginalScore(move.getOriginalScore());
        this.setFinalScore(move.getFinalScore());
        this.setCreationDate(LocalDateTime.now());
    }
}
