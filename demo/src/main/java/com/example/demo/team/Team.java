package com.example.demo.team;

import com.example.demo.player.Player;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();
    @Transient
    private int numberOfPlayers;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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
