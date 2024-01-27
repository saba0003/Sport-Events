package com.example.demo.team;

import com.example.demo.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
//@ToString
public class Team {

    private static final Logger logger = LogManager.getLogger(Team.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String city;
    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();
    @Transient
    private int numberOfPlayers;

    public Team() {
        logger.info("Team created!");
    }

    public Team(String name, String city) {
        if (name == null || city == null) {
            logger.error("Invalid name or city!");
            throw new IllegalArgumentException("Name or city can't be null!");
        }
        this.name = name;
        this.city = city;
        logger.info("Team created!");
    }

    public Team(String name, String city, List<Player> players) {
        if (name == null || city == null) {
            logger.error("Invalid name or city!");
            throw new IllegalArgumentException("Name or city can't be null!");
        }
        this.name = name;
        this.city = city;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setPlayer(Player player) {
        players.add(player);
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numberOfPlayers = numOfPlayers;
    }

    public void incrementNumOfPlayers() {
        numberOfPlayers++;
    }

    public void decrementNumOfPlayers() {
        numberOfPlayers--;
    }
}
