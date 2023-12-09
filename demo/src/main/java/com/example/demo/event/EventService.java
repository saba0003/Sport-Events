package com.example.demo.event;

import com.example.demo.team.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Service
public class EventService {

    private static final Logger logger = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventById(Long eventId) {
        if (eventId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            logger.info(String.format("Event with ID %d doesn't exist!", eventId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        return eventRepository.findById(eventId).get();
    }

    public List<Event> getEvents() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty())
            logger.info("No event found!");
        return events;
    }

    public Event getEventByName(String name) {
        if (name == null) {
            logger.error("Invalid name!");
            throw new IllegalArgumentException("Invalid name!");
        }
        if (eventRepository.findEventByName(name) == null) {
            logger.info(String.format("Event with name %s doesn't exist!", name));
            throw new IllegalArgumentException("Wrong name!");
        }
        return eventRepository.findEventByName(name);
    }

    public List<Event> getEventsByStartingDate(LocalDate startDate) {
        if (startDate == null) {
            logger.error("Invalid start date!");
            throw new IllegalArgumentException("Start date cannot be null!");
        }
        List<Event> events = eventRepository.findByStartingDate(startDate);
        if (events.isEmpty())
            logger.info("No event scheduled on this date.");
        return events;
    }

    public void addNewEvent(Event event) {
        if (event == null) {
            logger.error("Invalid event!");
            throw new IllegalArgumentException("Invalid event!");
        }
        if (eventRepository.existsById(event.getId())) {
            logger.error("Event with this ID already exists!");
            Scanner sc = new Scanner(System.in);
            System.out.println("Do you want to overwrite the existing player? y/n");
            String input = sc.nextLine().toLowerCase();
            sc.close();
            if (input.equals("y"))
                updateEvent(event.getId(), event.getName(), event.getTeam1(), event.getTeam2(), event.getStartDate());
            return;
        }
        eventRepository.save(event);
        logger.info("Event successfully added!");
    }

    public void deleteEvent(Long eventId) {
        Event event = getEventById(eventId);
        Scanner sc = new Scanner(System.in);
        System.out.printf("Are you sure you want to delete event: %s? y/n%n", event.getName());
        String input = sc.nextLine().toLowerCase();
        sc.close();
        if (input.equals("y")) {
            eventRepository.deleteById(eventId);
            logger.info("Event deleted.");
        }
    }

    @Transactional
    public void updateEvent(Long eventId, String name, Team team1, Team team2, LocalDateTime startDate) {
        if (eventId == null) {
            logger.error("Invalid Event ID!");
            throw new IllegalArgumentException("Event ID cannot be null");
        }

        if (name == null) {
            logger.error("Invalid Name!");
            throw new IllegalArgumentException("Event name cannot be null!");
        } else if (name.trim().isEmpty()) {
            logger.error("Invalid Name!");
            throw new IllegalArgumentException("Event name cannot be empty!");
        }

        if (team1 == null) {
            logger.error("Invalid Team!");
            throw new IllegalArgumentException("Team 1 cannot be null!");
        }

        if (team2 == null) {
            logger.error("Invalid Team!");
            throw new IllegalArgumentException("Team 2 cannot be null!");
        }

        if (startDate == null) {
            logger.error("Invalid start date!");
            throw new IllegalArgumentException("Start date cannot be null!");
        } else if (startDate.isBefore(LocalDateTime.now())) {
            logger.error("Invalid start date!");
            throw new IllegalArgumentException("Start date cannot be in the past!");
        }

        Event event = getEventById(eventId);
        event.setName(name);
        event.setTeam1(team1);
        event.setTeam2(team2);
        event.setStartDate(startDate);
        logger.info("Event successfully updated!");
    }
}
