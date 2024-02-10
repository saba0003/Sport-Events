package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import com.example.demo.util.Score;
import com.example.demo.util.ScoreBoardOperations;
import com.example.demo.util.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class EventService {

    private static final Logger logger = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public EventService(EventRepository eventRepository, TeamRepository teamRepository) {
        this.eventRepository = eventRepository;
        this.teamRepository = teamRepository;
    }

    public Event getEventById(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        return eventRepository.findById(eventId).get();
    }

    public Event getEventByTitle(String title) {
        if (title == null) {
            logger.error("Invalid title!");
            throw new IllegalArgumentException("Invalid title!");
        }
        if (eventRepository.findEventByTitle(title) == null) {
            logger.error(String.format("Event with the title %s doesn't exist!", title));
            throw new IllegalArgumentException("Wrong title!");
        }
        return eventRepository.findEventByTitle(title);
    }

    public List<Event> getEventsByParticipatingTeam(Team team) {
        if (team == null) {
            logger.error("Invalid team!");
            throw new IllegalArgumentException("Invalid team!");
        }
        if (eventRepository.findByParticipatingTeam(team) == null) {
            logger.error(String.format("Event with the team %s in it doesn't exist!", team.getName()));
            throw new IllegalArgumentException("Wrong team!");
        }
        return eventRepository.findByParticipatingTeam(team);
    }

    public List<Event> getEventsByStartingDate(LocalDate startDate) {
        if (startDate == null) {
            logger.error("Invalid start date!");
            throw new IllegalArgumentException("Invalid start date!");
        }
        List<Event> events = eventRepository.findByStartingDate(startDate);
        if (events.isEmpty())
            logger.info("No event scheduled on this date.");
        return events;
    }

    public List<Event> getEventsByStatus(Status status) {
        if (status == null) {
            logger.error("Invalid status!");
            throw new IllegalArgumentException("Invalid status!");
        }
        List<Event> events = eventRepository.findByStatus(status);
        if (events.isEmpty())
            logger.info("No event found with this status");
        return events;
    }

    public List<Event> getEvents() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty())
            logger.info("No event found!");
        return events;
    }

    public Event addNewEvent(Event event) {
        if (event == null) {
            logger.error("Invalid event!");
            throw new IllegalArgumentException("Invalid event!");
        }
        if (eventRepository.existsById(event.getId())) {
            logger.warn("Event with this ID already exists!");
            return event;
        }
        eventRepository.save(event);
        logger.info("Event successfully added!");
        return event;
    }

    public void deleteEvent(Long eventId) {
        getEventById(eventId);
        eventRepository.deleteById(eventId);
        logger.info("Event deleted.");
    }

    @Transactional
    public Event updateEvent(Long eventId, Event event) {
        Event actual = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        Team team1 = teamRepository.findById(event.getTeam1().getId()).orElseThrow(() -> new IllegalArgumentException("Team 1 couldn't be found!"));
        Team team2 = teamRepository.findById(event.getTeam2().getId()).orElseThrow(() -> new IllegalArgumentException("Team 2 couldn't be found!"));

        if (actual.equals(event)) {
            logger.info("Event already exists!");
            return actual;
        }

        actual.setTitle(event.getTitle());
        actual.setTeam1(team1);
        actual.setTeam2(team2);
        actual.setStartDate(event.getStartDate());

        logger.info("Event successfully updated!");

        return actual;
    }

    public void startEvent(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        if (event.getStatus().equals(Status.ONGOING)) {
            logger.warn("Game is already started!");
            return;
        }
        event.start();
        logger.info("Game started.");
    }

    public void stopTime(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        if (event.getStatus().equals(Status.PAUSED)) {
            logger.warn("Game is already paused!");
            return;
        }
        event.stop();
        logger.info("Game paused.");
    }

    public void resumeTime(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        if (event.getStatus().equals(Status.ONGOING)) {
            logger.warn("Game is already ongoing!");
            return;
        }
        event.resume();
        logger.info("Game unpaused.");
    }

    public void finishEvent(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        if (event.getStatus().equals(Status.COMPLETED)) {
            logger.warn("Game is already finished!");
            return;
        }
        event.finish();
        logger.info("Game finished.");
    }

    public void cancelEvent(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        if (event.getStatus().equals(Status.CANCELED)) {
            logger.warn("Game is already canceled!");
            return;
        }
        event.cancel();
        logger.info("Game canceled.");
    }

    public Score getScore(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));

        if (event.getScore() == null && event.getStatus().name().equals("CANCELED")) {
            logger.warn("Game is canceled.");
            return null;
        }
        if (event.getScore() == null && event.getStatus().name().equals("UNSCHEDULED")) {
            logger.warn("Game is not Started yet.");
            return null;
        }
        return event.getScore();
    }

    public void updateScoreBoard(Long eventId, ScoreBoardOperations side_operation) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (!eventRepository.existsById(eventId)) {
            logger.warn(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        if (side_operation == null) {
            logger.error("Invalid operation!");
            throw new IllegalArgumentException("Invalid operation!");
        }
        if (!Arrays.asList(ScoreBoardOperations.values()).contains(side_operation)) {
            logger.error("Illegal request!");
            throw new IllegalArgumentException("Illegal request!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event couldn't be found!"));
        event.updateScoreBoard(side_operation);
        logger.info("Score board updated successfully.");
    }
}
