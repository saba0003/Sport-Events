package com.example.demo.player;

import com.example.demo.team.Team;
import com.example.demo.util.FullName;
import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Transient
    private FullName fullName;
    private Integer number;
    @ManyToOne(optional = true)
    private Team team;

    public Player() {
    }

    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(String firstName, String lastName, Team team, Integer number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public FullName getFullName() {
        return new FullName(firstName, lastName);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if (this.team == null) {
            this.team = team;
            return;
        }
        Team oldTeam = this.team;
        // TODO: Properly remove the player from playersList of the old team
        // TODO: Properly add the player to the new team
        this.team = team;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "player";
    }
}
