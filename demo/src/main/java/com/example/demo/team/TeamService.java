package com.example.demo.team;

import com.example.demo.player.Player;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class TeamService {

    private static final Logger logger = LogManager.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamById(Long teamId) {
        if (teamId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (teamRepository.findById(teamId).isEmpty()) {
            logger.info(String.format("Team with ID %d doesn't exist!", teamId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        return teamRepository.findById(teamId).get();
    }

    public Team getTeamByName(String name) {
        if (name == null) {
            logger.error("Invalid name!");
            throw new IllegalArgumentException("Invalid name!");
        }
        Team team = teamRepository.findByName(name);
        if (team == null) {
            logger.info(String.format("Team with name %s doesn't exist!", name));
            throw new IllegalArgumentException("Wrong name!");
        }
        return team;
    }

//    public List<Team> getTeamsByNumberOfPlayers(Integer numOfPlayers) {
//        if (numOfPlayers == null) {
//            logger.error("Invalid number!");
//            throw new IllegalArgumentException("Invalid number!");
//        }
//        if (numOfPlayers < 0) {
//            logger.error("Player number can't be negative!");
//            throw new IllegalArgumentException("Invalid number!");
//        }
//        return teamRepository.findByNumberOfPlayers(numOfPlayers);
//    }

    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    public void addNewTeam(Team team) {
//        if (team == null) {
//            logger.error("Invalid team!");
//            throw new IllegalArgumentException("Invalid team!");
//        }
//        if (teamRepository.existsById(team.getId())) {
//            logger.error("Team with this ID already exists!");
//            System.out.println("Do you want to overwrite the existing team? y/n");
//            Scanner sc = new Scanner(System.in);
//            String input = sc.nextLine().toLowerCase();
//            sc.close();
//            if (input.equals("y"))
//                updateTeam(team.getId(), team.getName(), team.getPlayers());
//            return;
//        }
        teamRepository.save(team);
        logger.info("Team successfully added!");
    }

    public void deleteTeam(Long teamId) {
        // Maybe warn manager before deleting a team
        getTeamById(teamId);
        teamRepository.deleteById(teamId);
        logger.info("Team deleted");
    }

    @Transactional // jakarta or spring package?
    public void updateTeam(Long teamId, String name, List<Player> players) {
        Team team = getTeamById(teamId);
        // TODO: Should add logic for exceptions
        if (name != null && name.length() > 0 && !team.getName().equals(name))
            team.setName(name);
        if (players != null)
            team.setPlayers(players);
        logger.info("Team successfully updated!");
    }
}
