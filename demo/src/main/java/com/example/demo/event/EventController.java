package com.example.demo.event;

import com.example.demo.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping
    public void registerNewEvent(@RequestBody Event event) {
        eventService.addNewEvent(event);
    }

    @DeleteMapping(path = {"eventId"})
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }

    @PutMapping(path = {"eventId"})
    public void updateEvent(@PathVariable Long eventId,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) Team team1,
                            @RequestParam(required = false) Team team2,
                            @RequestParam(required = false) LocalDateTime startDate) {
        eventService.updateEvent(eventId, name, team1, team2, startDate);
    }
}
