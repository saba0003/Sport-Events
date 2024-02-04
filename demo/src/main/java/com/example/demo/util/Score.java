package com.example.demo.util;

import jakarta.persistence.Embeddable;

@Embeddable
public class Score {

    private int homeTeamScore;
    private int awayTeamScore;

    public Score() {
        homeTeamScore = awayTeamScore = 0;
    }

    public Score(int homeTeamScore, int awayTeamScore) {
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public void incrementHomeTeamScore() {
        homeTeamScore++;
    }

    public void decrementHomeTeamScore() {
        homeTeamScore--;
    }

    public void incrementAwayTeamScore() {
        awayTeamScore++;
    }

    public void decrementAwayTeamScore() {
        awayTeamScore--;
    }

    public void defaultScore() {
        homeTeamScore = awayTeamScore = 0;
    }

    @Override
    public String toString() {
        return homeTeamScore + " - " + awayTeamScore;
    }
}
