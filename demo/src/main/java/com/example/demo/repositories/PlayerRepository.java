package com.example.demo.repositories;

import com.example.demo.models.Player;
import com.example.demo.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByFirstName(String firstName);
    List<Player> findByLastName(String lastName);
    List<Player> findByTeam(Team team);
    List<Player> findByTeamName(String name);
    List<Player> findByTeamCity(String city);
    List<Player> findByJerseyNumber(Integer jerseyNumber);
    Player findByTeamAndJerseyNumber(Team team, Integer jerseyNumber);
    Player findByTeamNameAndJerseyNumber(String teamName, Integer jerseyNumber);
}
