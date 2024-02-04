package com.example.demo.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> listEvents() {
        return eventService.getEvents();
    }

    @GetMapping(path = "{eventId}")
    public ResponseEntity<Event> getSpecificEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEventById(eventId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Event> registerNewEvent(@RequestBody Event event) {
        return new ResponseEntity<>(eventService.addNewEvent(event), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{eventId}/delete")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>("Event deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{eventId}/update")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId,
                            @RequestBody Event event) {
        Event response = eventService.updateEvent(eventId, event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
