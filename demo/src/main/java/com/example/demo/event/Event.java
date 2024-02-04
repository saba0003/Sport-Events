package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.util.Score;
import com.example.demo.util.ScoreBoardOperations;
import com.example.demo.util.Status;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Team team1;
    @NonNull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Team team2;
    private LocalDateTime startDate;
    private Status status;
    @Embedded
    private Score score;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    private Team winner;

    public Event() {
    }

    public Event(String title, Team team1, Team team2) {
        if (title == null || team1 == null || team2 == null)
            throw new IllegalArgumentException("Title or any participating teams can't be null!");
        this.title = title;
        this.team1 = team1;
        this.team2 = team2;
        status = Status.UNSCHEDULED;
    }

    public Event(String title, Team team1, Team team2, LocalDateTime startDate) {
        if (title == null || team1 == null || team2 == null)
            throw new IllegalArgumentException("Title or any participating teams can't be null!");
        if (startDate == null)
            throw new IllegalArgumentException("Start date can't be null!");
        if (startDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Start date must be in the future!");
        this.title = title;
        this.team1 = team1;
        this.team2 = team2;
        this.startDate = startDate;
        status = Status.SCHEDULED;
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
        if (startDate == null)
            status = Status.UNSCHEDULED;
        if (startDate != null)
            status = Status.SCHEDULED;
        this.startDate = startDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void start() {
        status = Status.ONGOING;
        score = new Score();
    }

    public void stop() {
        status = Status.PAUSED;
    }

    public void resume() {
        status = Status.ONGOING;
    }

    public void finish() {
        winner = determineWinner();
        status = Status.COMPLETED;
    }

    public void cancel() {
        status = Status.CANCELED;
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

    public void updateScoreBoard(ScoreBoardOperations side_operation) {
        switch (side_operation) {
            case HOME_DECREMENT -> score.decrementHomeTeamScore();
            case HOME_INCREMENT -> score.incrementHomeTeamScore();
            case AWAY_DECREMENT -> score.decrementAwayTeamScore();
            case AWAY_INCREMENT -> score.incrementAwayTeamScore();
            case DEFAULT -> score.defaultScore();
        }
    }

    private Team determineWinner() {
        if (score.getHomeTeamScore() > score.getAwayTeamScore())
            return team1;
        else if (score.getHomeTeamScore() < score.getAwayTeamScore())
            return team2;
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Event other = (Event) obj;

        if (!Objects.equals(this.title, other.title)) return false;

        return true;
    }
}
