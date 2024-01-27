package com.example.demo.player;

import com.example.demo.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "players")
// @ToString
// Including this annotation resulted in infinite recursions while debugging testers.
// Which also means that it slows down runtime, so I'm Checking this out until further notice.
// Manually overriding `toString()` method didn't help either.
// Same thing happened when I was trying to retrieve data from `Team` class.
// Seemingly the problem is bidirectional (@OneToMany) relationship between entities, but I'm not sure.
public class Player {

    private static final Logger logger = LogManager.getLogger(Player.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Transient
    private String fullName;
//    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "team_id")
    private Team team;
    @Nullable
    private Integer jerseyNumber;

    public Player() {
        logger.info("Player created!");
    }

    public Player(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            logger.error("Invalid firstname or lastname!");
            throw new IllegalArgumentException("Firstname and lastname can't be null!");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        logger.info("Player created!");
    }

    public Player(String firstName, String lastName, Team team, Integer jerseyNumber) {
        if (firstName == null || lastName == null) {
            logger.error("Invalid firstname or lastname!");
            throw new IllegalArgumentException("Firstname and lastname can't be null!");
        }
        if (team != null && jerseyNumber == null) {
            logger.error("Invalid jersey number!");
            throw new IllegalArgumentException("When in team, jersey number can't be null!");
        }
        if (team != null && jerseyNumber < 1) {
            logger.error("Invalid jersey number!");
            throw new IllegalArgumentException("Player numbering begins from 1 upwards!");
        }
        else if (team == null && jerseyNumber != null) {
            logger.error("Invalid team!");
            throw new IllegalArgumentException("If player has jersey number, he/she must be in a team!");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.jerseyNumber = jerseyNumber;
        logger.info("Player created!");
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
}
