package com.example.demo.services;

import com.example.demo.models.Player;
import com.example.demo.models.Team;

import java.util.List;

public interface PlayerService {
    Player getPlayerById(Long playerId);
    List<Player> getPlayersByFirstName(String firstName);
    List<Player> getPlayersByLastName(String lastName);
    List<Player> getPlayersByTeam(Team team);
    List<Player> getPlayersByTeamName(String name);
    List<Player> getPlayersByCity(String city);
    List<Player> getPlayersByNumber(Integer jerseyNumber);
    Player getPlayerByTeamAndNumber(Team team, Integer jerseyNumber);
    Player getPlayerByTeamNameAndNumber(String name, Integer jerseyNumber);
    List<Player> getPlayers();
    Player addNewPlayer(Player player);
    void deletePlayer(Long playerId);
    Player updatePlayer(Long playerId, Player player);
}
