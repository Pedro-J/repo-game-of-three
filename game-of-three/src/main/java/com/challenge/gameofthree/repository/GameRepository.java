package com.challenge.gameofthree.repository;

import com.challenge.gameofthree.domain.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {

    @Query("select ge from GameEntity ge left join ge.moves m where ge.id = :id order by m.creationDate")
    Optional<GameEntity> findByIdWithMoves(@Param("id") Long id);

    @Query("select ge from GameEntity ge where ge.ackPlayer1 is false or ge.ackPlayer2 is false")
    Optional<GameEntity> findCurrentGame();

}