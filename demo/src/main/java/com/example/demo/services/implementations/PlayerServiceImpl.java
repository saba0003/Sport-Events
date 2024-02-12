package com.example.demo.services.implementations;

import com.example.demo.models.Player;
import com.example.demo.repositories.PlayerRepository;
import com.example.demo.models.Team;
import com.example.demo.services.PlayerService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = LogManager.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Player> getPlayers() {
        List<Player> players = playerRepository.findAll();
        if (players.isEmpty())
            logger.info("No player found!");
        return players;
    }

    @Override
    public Player addNewPlayer(Player player) {
        if (player == null) {
            logger.error("Invalid player!");
            throw new IllegalArgumentException("Invalid player!");
        }
        if (player.getId() == null) {
            playerRepository.save(player);
            logger.info("Player successfully added!");
        } else {
            if (playerRepository.existsById(player.getId())) {
                logger.warn("Player with this ID already exists!");
            } else {
                playerRepository.save(player);
                logger.info("Player successfully added!");
            }
        }
        return player;
    }

    @Override
    public void deletePlayer(Long playerId) {
        getPlayerById(playerId);
        playerRepository.deleteById(playerId);
        logger.info("Player deleted");
    }

    @Override
    @Transactional
    public Player updatePlayer(Long playerId, Player player) {
        Player actual = playerRepository.findById(playerId).orElseThrow(() -> new IllegalArgumentException("Player couldn't be found!"));

        if (actual.equals(player)) {
            logger.info("Player already exists!");
            return actual;
        }

        actual.setFirstName(player.getFirstName());
        actual.setLastName(player.getLastName());
        actual.setTeam(player.getTeam());
        actual.setJerseyNumber(player.getJerseyNumber());

        logger.info("Player successfully updated!");

        return actual;
    }
}
