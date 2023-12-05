package com.example.demo.player;

import com.example.demo.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByFirstName(String firstName);
    List<Player> findByLastName(String lastName);
    List<Player> findByFullName(String fullName);
    List<Player> findByTeam(Team team);
    List<Player> findByTeamName(String TeamName);
    List<Player> findByNumber(Integer number);
    Player findByTeamAndNumber(Team team, Integer number);
    Player findByTeamNameAndNumber(String teamName, Integer number);
}
