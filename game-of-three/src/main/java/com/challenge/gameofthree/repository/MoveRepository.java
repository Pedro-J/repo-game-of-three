package com.challenge.gameofthree.repository;

import com.challenge.gameofthree.domain.GameEntity;
import com.challenge.gameofthree.domain.MoveEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MoveRepository extends CrudRepository<MoveEntity, Long> {

    List<MoveEntity> findByGame(GameEntity gameEntity);
}
