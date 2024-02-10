package com.example.demo.event;

import com.example.demo.util.Score;
import com.example.demo.util.ScoreBoardOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('SPECTATOR', 'COACH', 'ADMIN')")
    public List<Event> listEvents() {
        return eventService.getEvents();
    }

    @GetMapping(path = "{eventId}")
    @PreAuthorize("hasAnyAuthority('SPECTATOR', 'COACH', 'ADMIN')")
    public ResponseEntity<Event> getSpecificEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEventById(eventId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Event> registerNewEvent(@RequestBody Event event) {
        return new ResponseEntity<>(eventService.addNewEvent(event), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{eventId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>("Event deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{eventId}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId,
                                             @RequestBody Event event) {
        Event response = eventService.updateEvent(eventId, event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "{eventId}/start")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> startEvent(@PathVariable Long eventId) {
        eventService.startEvent(eventId);
        return new ResponseEntity<>("Game has started.", HttpStatus.OK);
    }

    @PostMapping(path = "{eventId}/stop")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> stopEvent(@PathVariable Long eventId) {
        eventService.stopTime(eventId);
        return new ResponseEntity<>("Game is paused.", HttpStatus.OK);
    }

    @PostMapping(path = "{eventId}/resume")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> resumeEvent(@PathVariable Long eventId) {
        eventService.resumeTime(eventId);
        return new ResponseEntity<>("Game is unpaused.", HttpStatus.OK);
    }

    @PostMapping(path = "{eventId}/finish")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> finishEvent(@PathVariable Long eventId) {
        eventService.finishEvent(eventId);
        return new ResponseEntity<>("Game is finished.", HttpStatus.OK);
    }

    @PostMapping(path = "{eventId}/cancel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> cancelEvent(@PathVariable Long eventId) {
        eventService.cancelEvent(eventId);
        return new ResponseEntity<>("Game is canceled.", HttpStatus.OK);
    }

    @PutMapping(path = "{eventId}/score/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateScore(@PathVariable Long eventId,
                                              @RequestBody ScoreBoardOperations side_operation) {
        eventService.updateScoreBoard(eventId, side_operation);
        return new ResponseEntity<>("Score board is updated.", HttpStatus.OK);
    }

    @GetMapping(path = "{eventId}/score")
    @PreAuthorize("hasAnyAuthority('SPECTATOR', 'COACH', 'ADMIN')")
    public ResponseEntity<String> viewScore(@PathVariable Long eventId) {
        Score score = eventService.getScore(eventId);
        return new ResponseEntity<>(score.toString(), HttpStatus.OK);
    }
}
