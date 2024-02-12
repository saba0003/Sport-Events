package com.example.demo.services;

import com.example.demo.models.Team;

import java.util.List;

public interface TeamService {
    Team getTeamById(Long teamId);
    Team getTeamByName(String name);
    List<Team> getTeamByCity(String city);
    List<Team> getTeamsByNumberOfPlayers(Integer numOfPlayers);
    List<Team> getTeams();
    Team addNewTeam(Team team);
    void deleteTeam(Long teamId);
    Team updateTeam(Long teamId, Team team);
}
