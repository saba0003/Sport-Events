package com.example.demo.services.implementations;

import com.example.demo.repositories.TeamRepository;
import com.example.demo.models.Team;
import com.example.demo.services.TeamService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private static final Logger logger = LogManager.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Team> getTeams() {
        List<Team> teams = teamRepository.findAll();
        if (teams.isEmpty())
            logger.info("No team found!");
        return teams;
    }

    @Override
    public Team addNewTeam(Team team) {
        if (team == null) {
            logger.error("Invalid team!");
            throw new IllegalArgumentException("Invalid team!");
        }
        if (team.getId() == null) {
            teamRepository.save(team);
            logger.info("Team successfully added!");
        } else {
            if (teamRepository.existsById(team.getId())) {
                logger.error("Team with this ID already exists!");
            } else {
                teamRepository.save(team);
                logger.info("Team successfully added!");
            }
        }
        return team;
    }

    @Override
    public void deleteTeam(Long teamId) {
        getTeamById(teamId);
        teamRepository.deleteById(teamId);
        logger.info("Team deleted");
    }

    @Override
    @Transactional
    public Team updateTeam(Long teamId, Team team) {
        Team actual = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team couldn't be found!"));

        if (actual.equals(team)) {
            logger.info("Team already exists!");
            return actual;
        }

        actual.setName(team.getName());
        actual.setCity(team.getCity());
//        actual.setPlayers(team.getPlayers());

        logger.info("Team successfully updated!");

        return actual;
    }
}
