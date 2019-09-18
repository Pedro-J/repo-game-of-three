package com.challenge.gameofthree.repository;

import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends CrudRepository<MoveEntity, Long> {

    List<MoveEntity> findByGame(GameEntity gameEntity);
}
