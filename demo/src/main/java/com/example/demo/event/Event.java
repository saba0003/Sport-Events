package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.util.Score;
import com.example.demo.util.Status;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    private static final Logger logger = LogManager.getLogger(Event.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String title;
    @ManyToOne(optional = false)
    private Team team1;
    @ManyToOne(optional = false)
    private Team team2;
    private LocalDateTime startDate;
    private Status status;
    @Embedded
    private Score score;
    @ManyToOne(optional = true)
    private Team winner;

    public Event() {
        logger.info("Event created!");
    }

    public Event(String title, Team team1, Team team2) {
        if (title == null || team1 == null || team2 == null) {
            logger.error("Invalid title or teams!");
            throw new IllegalArgumentException("Title or any participating teams can't be null!");
        }
        this.title = title;
        this.team1 = team1;
        this.team2 = team2;
        status = Status.UNSCHEDULED;
        logger.info("Event created!");
    }

    public Event(String title, Team team1, Team team2, LocalDateTime startDate) {
        if (title == null || team1 == null || team2 == null) {
            logger.error("Invalid title or teams!");
            throw new IllegalArgumentException("Title or any participating teams can't be null!");
        }
        if (startDate == null) {
            logger.error("Invalid start date!");
            throw new IllegalArgumentException("Start date cannot be null!");
        }
        if (startDate.isBefore(LocalDateTime.now())) {
            logger.error("Invalid start date!");
            throw new IllegalArgumentException("Start date must be in the future!");
        }
        this.title = title;
        this.team1 = team1;
        this.team2 = team2;
        this.startDate = startDate;
        status = Status.SCHEDULED;
        logger.info("Event created!");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }
}
