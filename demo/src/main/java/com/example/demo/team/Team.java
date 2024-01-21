package com.example.demo.team;

import com.example.demo.player.Player;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
//@ToString
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();
    @Transient
    private int numberOfPlayers;

    public Team() {
    }

    public Team(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Team(String name, String city, List<Player> players) {
        this.name = name;
        this.city = city;
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
