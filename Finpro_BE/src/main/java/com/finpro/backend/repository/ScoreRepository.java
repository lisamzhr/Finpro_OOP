package com.finpro.backend.repository;

import com.finpro.backend.model.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {
    //methode non query
    List<Score> findByPlayerId(UUID playerId);
    List<Score> findByPlayerIdOrderByValueDesc(UUID playerId);
    List<Score> findByValueGreaterThan(Integer value);
    List<Score> findAllByOrderByCreatedAtDesc();

    //method query
    @Query("SELECT s FROM Score s ORDER BY s.value DESC")
    List<Score> findTopScores(Integer limit);
    @Query("SELECT s FROM Score s WHERE s.playerId = :playerId ORDER BY s.value DESC")
    List<Score> findHighestScoreByPlayerId(@Param("playerId") UUID playerId);
    @Query("SELECT SUM(s.coinsCollected) FROM Score s WHERE s.playerId =:playerId")
    Integer getTotalCoinsByPlayerId(@Param("playerId") UUID playerId);
    @Query("SELECT SUM(s.distanceTravelled) FROM Score s WHERE s.playerId =:playerId")
    Integer getTotalDistanceByPlayerId(@Param("playerId") UUID playerId);
}