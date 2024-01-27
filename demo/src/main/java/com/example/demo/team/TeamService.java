package com.example.demo.team;

import com.example.demo.player.Player;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Team> getTeamByCity(String city) {
        if (city == null) {
            logger.error("Invalid city!");
            throw new IllegalArgumentException("Invalid city!");
        }
        List<Team> teams = teamRepository.findByCity(city);
        if (teams == null) {
            logger.info(String.format("There is no team in %s!", city));
            throw new IllegalArgumentException("Wrong city!");
        }
        return teams;
    }

    public List<Team> getTeamsByNumberOfPlayers(Integer numOfPlayers) {
        if (numOfPlayers == null) {
            logger.error("Invalid number!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (numOfPlayers < 0) {
            logger.error("Player number can't be negative!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (teamRepository.findByNumberOfPlayers(numOfPlayers).isEmpty()) {
            logger.info("Team with the given number of players doesn't exist");
            throw new IllegalArgumentException("Wrong number!");
        }
        return teamRepository.findByNumberOfPlayers(numOfPlayers);
    }

    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    public void addNewTeam(Team team) {
        if (team == null) {
            logger.error("Invalid team!");
            throw new IllegalArgumentException("Invalid team!");
        }
        if (teamRepository.existsById(team.getId())) {
            logger.error("Team with this ID already exists!");
            return;
        }
        teamRepository.save(team);
        logger.info("Team successfully added!");
    }

    public void deleteTeam(Long teamId) {
        getTeamById(teamId);
        teamRepository.deleteById(teamId);
        logger.info("Team deleted");
    }

    @Transactional
    public void updateTeam(Long teamId, String name, String city, List<Player> players) {
        Team team = getTeamById(teamId);

        if (name == null || name.trim().isEmpty()) {
            logger.error("Invalid name!");
            throw new IllegalArgumentException("Name can't be null or empty!");
        }
        if (!team.getName().equals(name))
            team.setName(name);

        if (city == null || city.trim().isEmpty()) {
            logger.error("Invalid city!");
            throw new IllegalArgumentException("City can't be null or empty!");
        }
        if (!team.getCity().equals(city))
            team.setCity(city);

        if (!team.getPlayers().equals(players))
            team.setPlayers(players);

        logger.info("Team successfully updated!");
    }
}
