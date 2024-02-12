package com.example.demo.repositories;

import com.example.demo.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
    List<Team> findByCity(String city);
    @Query("SELECT t FROM Team t WHERE SIZE(t.players) = :numberOfPlayers")
    List<Team> findByNumberOfPlayers(@Param("numberOfPlayers") Integer numberOfPlayers);
}
