package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teams")
public class Team {

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
    }

    public Team(String name, String city) {
        if (name == null || city == null)
            throw new IllegalArgumentException("Name or city can't be null!");
        this.name = name;
        this.city = city;
    }

    public Team(String name, String city, List<Player> players) {
        if (name == null || city == null)
            throw new IllegalArgumentException("Name or city can't be null!");
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Team other = (Team) obj;

        if (!Objects.equals(this.name, other.name)) return false;
        if (!Objects.equals(this.city, other.city)) return false;
        if (!Objects.equals(this.players, other.players)) return false;

        return true;
    }
}
