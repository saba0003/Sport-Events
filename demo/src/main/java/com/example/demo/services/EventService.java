package com.example.demo.services;

import com.example.demo.models.Event;
import com.example.demo.models.Team;
import com.example.demo.util.Score;
import com.example.demo.util.ScoreBoardOperations;
import com.example.demo.util.Status;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    Event getEventById(Long eventId);
    Event getEventByTitle(String title);
    List<Event> getEventsByParticipatingTeam(Team team);
    List<Event> getEventsByStartingDate(LocalDate startDate);
    List<Event> getEventsByStatus(Status status);
    List<Event> getEvents();
    Event addNewEvent(Event event);
    void deleteEvent(Long eventId);
    Event updateEvent(Long eventId, Event event);
    void startEvent(Long eventId);
    void stopTime(Long eventId);
    void resumeTime(Long eventId);
    void finishEvent(Long eventId);
    void cancelEvent(Long eventId);
    Score getScore(Long eventId);
    void updateScoreBoard(Long eventId, ScoreBoardOperations side_operation);
}
