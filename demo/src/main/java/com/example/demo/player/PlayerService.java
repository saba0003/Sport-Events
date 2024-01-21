package com.example.demo.player;

import com.example.demo.team.Team;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

import static com.example.demo.util.ExceptionHandler.*;

@Service
public class PlayerService {

    private static final Logger logger = LogManager.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getPlayerById(Long playerId) {
        if (playerId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (playerRepository.findById(playerId).isEmpty()) {
            logger.info(String.format("Player with ID %d doesn't exist!", playerId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        return playerRepository.findById(playerId).get();
    }

    public List<Player> getPlayersByFirstName(String firstName) {
        if (firstName == null) {
            logger.error("Invalid firstname!");
            throw new IllegalArgumentException("Invalid firstname!");
        }
        List<Player> players = playerRepository.findByFirstName(firstName);
        if (players.isEmpty())
            logger.info("No players found with firstname: " + firstName);
        return players;
    }

    public List<Player> getPlayersByLastName(String lastName) {
        if (lastName == null) {
            logger.error("Invalid lastname!");
            throw new IllegalArgumentException("Invalid lastname!");
        }
        List<Player> players = playerRepository.findByLastName(lastName);
        if (players.isEmpty())
            logger.info("No players found with lastname: " + lastName);
        return players;
    }

//    public List<Player> getPlayersByFullName(FullName fullName) {
//        if (fullName == null) {
//            logger.error("Invalid full-name!");
//            throw new IllegalArgumentException("Invalid full-name!");
//        }
//        List<Player> players = playerRepository.findByFullName(fullName.toString());
//        if (players.isEmpty())
//            logger.info("No players found with full-name: " + fullName);
//        return players;
//    }

    public List<Player> getPlayersByTeam(Team team) {
        if (team == null) {
            logger.error("Invalid team!");
            throw new IllegalArgumentException("Invalid team!");
        }
        List<Player> players = playerRepository.findByTeam(team);
        if (players.isEmpty())
            logger.info("No players found in team: " + team);
        return players;
    }

    public List<Player> getPlayersByTeamName(String name) {
        if (name == null) {
            logger.error("Invalid name!");
            throw new IllegalArgumentException("Invalid name!");
        }
        List<Player> players = playerRepository.findByTeamName(name);
        if (players.isEmpty())
            logger.info("No players found in team with name: " + name);
        return players;
    }

    public List<Player> getPlayersByCity(String city) {
        if (city == null) {
            logger.error("Invalid city!");
            throw new IllegalArgumentException("Invalid city!");
        }
        List<Player> players = playerRepository.findByTeamCity(city);
        if (players.isEmpty())
            logger.info("No players found in " + city);
        return players;
    }

    public List<Player> getPlayersByNumber(Integer jerseyNumber) {
        if (jerseyNumber == null) {
            logger.error("Invalid number!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (jerseyNumber == 0) {
            logger.error("Player number can't be zero!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (jerseyNumber < 0) {
            logger.error("Player number can't be negative!");
            throw new IllegalArgumentException("Invalid number!");
        }
        List<Player> players = playerRepository.findByJerseyNumber(jerseyNumber);
        if (players.isEmpty())
            logger.info("No Players found with number: " + jerseyNumber);
        return players;
    }

    public Player getPlayerByTeamAndNumber(Team team, Integer jerseyNumber) {
        if (team == null) {
            logger.error("Invalid team!");
            throw new IllegalArgumentException("Invalid team!");
        }
        if (jerseyNumber == null) {
            logger.error("Invalid number!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (jerseyNumber == 0) {
            logger.error("Player number can't be zero!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (jerseyNumber < 0) {
            logger.error("Player number can't be negative!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (playerRepository.findByTeam(team).isEmpty())
            logger.info("No players found in team: " + team);
        if (playerRepository.findByJerseyNumber(jerseyNumber).isEmpty())
            logger.info("No Players found with number: " + jerseyNumber);
        return playerRepository.findByTeamAndJerseyNumber(team, jerseyNumber);
    }

    public Player getPlayerByTeamNameAndNumber(String name, Integer jerseyNumber) {
        if (name == null) {
            logger.error("Invalid team name!");
            throw new IllegalArgumentException("Invalid team name!");
        }
        if (playerRepository.findByTeamName(name).isEmpty())
            logger.info("No players found in team with name: " + name);
        if (jerseyNumber == null) {
            logger.error("Invalid number!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (jerseyNumber == 0) {
            logger.error("Player number can't be zero!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (jerseyNumber < 0) {
            logger.error("Player number can't be negative!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (playerRepository.findByJerseyNumber(jerseyNumber).isEmpty())
            logger.info("No Players found with number: " + jerseyNumber);
        return playerRepository.findByTeamNameAndJerseyNumber(name, jerseyNumber);
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public void addNewPlayer(Player player) {
        if (player == null) {
            logger.error("Invalid player!");
            throw new IllegalArgumentException("Invalid player!");
        }
        if (playerRepository.existsById(player.getId())) {
            logger.warn("Player with this ID already exists!");
            return;
        }
        playerRepository.save(player);
        logger.info("Player successfully added!");
    }

    public void deletePlayer(Long playerId) {
        getPlayerById(playerId);
        playerRepository.deleteById(playerId);
        logger.info("Player deleted");
    }

    @Transactional // jakarta or spring package?
    public void updatePlayer(Long playerId, String firstName, String lastName, Team team, Integer jerseyNumber) {
        Player player = getPlayerById(playerId);
        // TODO: Should add logic for exceptions
        if (firstName != null && firstName.length() > 0 && !player.getFirstName().equals(firstName))
            player.setFirstName(firstName);
        if (lastName != null && lastName.length() > 0 && !player.getLastName().equals(lastName))
            player.setLastName(lastName);
        if (team != null && !team.equals(player.getTeam()))
            player.setTeam(team);
        if (jerseyNumber != null && !jerseyNumber.equals(player.getJerseyNumber()))
            player.setJerseyNumber(jerseyNumber);
        logger.info("Player successfully updated!");
    }
}
