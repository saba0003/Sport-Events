package com.example.demo.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addNewEvent() {
        
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
}
