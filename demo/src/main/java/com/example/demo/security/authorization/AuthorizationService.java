package com.example.demo.security.authorization;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserService;
import com.example.demo.event.Event;
import com.example.demo.event.EventService;
import com.example.demo.player.Player;
import com.example.demo.player.PlayerService;
import com.example.demo.team.Team;
import com.example.demo.team.TeamService;
import com.example.demo.util.Score;
import com.example.demo.util.ScoreBoardOperations;
import com.example.demo.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuthorizationService {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final EventService eventService;
    private final AppUserService userService;

    @Autowired
    public AuthorizationService(PlayerService playerService,
                                TeamService teamService,
                                EventService eventService,
                                AppUserService userService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.eventService = eventService;
        this.userService = userService;
    }

    /** Player related methods */

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Player> getPlayerById(Long playerId) {
        return new ResponseEntity<>(playerService.getPlayerById(playerId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Player>> getPlayersByTeam(Team team) {
        return new ResponseEntity<>(playerService.getPlayersByTeam(team), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Player>> getPlayersByTeamName(String name) {
        return new ResponseEntity<>(playerService.getPlayersByTeamName(name), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Player>> getPlayersByCity(String city) {
        return new ResponseEntity<>(playerService.getPlayersByCity(city), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Player>> getPlayersByNumber(Integer number) {
        return new ResponseEntity<>(playerService.getPlayersByNumber(number), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<Player> getPlayerByTeamAndNumber(Team team, Integer jerseyNumber) {
        return new ResponseEntity<>(playerService.getPlayerByTeamAndNumber(team, jerseyNumber), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<Player> getPlayerByTeamNameAndNumber(String name, Integer jerseyNumber) {
        return new ResponseEntity<>(playerService.getPlayerByTeamNameAndNumber(name, jerseyNumber), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Player>> viewPlayers() {
        return new ResponseEntity<>(playerService.getPlayers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Player> addNewPlayer(Player player) {
        return new ResponseEntity<>(playerService.addNewPlayer(player), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePlayer(Long playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>("Player deleted successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Player> updatePlayer(Long playerId, Player player) {
        return new ResponseEntity<>(playerService.updatePlayer(playerId, player), HttpStatus.OK);
    }

    /** Team related methods */

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> getTeamById(Long teamId) {
        return new ResponseEntity<>(teamService.getTeamById(teamId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<Team> getTeamByName(String name) {
        return new ResponseEntity<>(teamService.getTeamByName(name), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Team>> getTeamByCity(String city) {
        return new ResponseEntity<>(teamService.getTeamByCity(city), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Team>> getTeamsByNumberOfPlayers(Integer numOfPlayers) {
        return new ResponseEntity<>(teamService.getTeamsByNumberOfPlayers(numOfPlayers), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Team>> viewTeams() {
        return new ResponseEntity<>(teamService.getTeams(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> addNewTeam(Team team) {
        return new ResponseEntity<>(teamService.addNewTeam(team), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTeam(Long teamId) {
        teamService.deleteTeam(teamId);
        return new ResponseEntity<>("Team deleted successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> updateTeam(Long teamId, Team team) {
        return new ResponseEntity<>(teamService.updateTeam(teamId, team), HttpStatus.OK);
    }

    /** Event related methods */

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> getEventById(Long eventId) {
        return new ResponseEntity<>(eventService.getEventById(eventId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<Event> getEventByTitle(String title) {
        return new ResponseEntity<>(eventService.getEventByTitle(title), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEventsByParticipatingTeam(Team team) {
        return new ResponseEntity<>(eventService.getEventsByParticipatingTeam(team), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEventsByStartingDate(LocalDate startDate) {
        return new ResponseEntity<>(eventService.getEventsByStartingDate(startDate), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEventsByStatus(Status status) {
        return new ResponseEntity<>(eventService.getEventsByStatus(status), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> viewEvents() {
        return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> addNewEvent(Event event) {
        return new ResponseEntity<>(eventService.addNewEvent(event), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteEvent(Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>("Event deleted successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> updateEvent(Long eventId, Event event) {
        return new ResponseEntity<>(eventService.updateEvent(eventId, event), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> startEvent(Event event) {
        eventService.StartEvent(event);
        return new ResponseEntity<>("Event is paused.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> stopEvent(Event event) {
        eventService.stopTime(event);
        return new ResponseEntity<>("Event is paused.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resumeEvent(Event event) {
        eventService.resumeTime(event);
        return new ResponseEntity<>("Event is unpaused.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> finishEvent(Event event) {
        eventService.finishEvent(event);
        return new ResponseEntity<>("Event is finished.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cancelEvent(Event event) {
        eventService.cancelEvent(event);
        return new ResponseEntity<>("Event is canceled.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateScore(Event event, ScoreBoardOperations side_operation) {
        eventService.updateScoreBoard(event, side_operation);
        return new ResponseEntity<>("Score board is updated.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SPECTATOR') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<String> viewScore(Event event) {
        Score score = eventService.getScore(event);
        if (event.getScore() == null && event.getStatus().name().equals("CANCELED"))
            return new ResponseEntity<>("Event is canceled.", HttpStatus.BAD_REQUEST);
        if (event.getScore() == null && event.getStatus().name().equals("UNSCHEDULED"))
            return new ResponseEntity<>("Event is not started yet.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(score.toString(), HttpStatus.OK);
    }

    /** CRUD operations for User entities */

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> getUserById(Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> getUserByUsername(String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUser>> viewUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> addNewUser(AppUser user) {
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted Successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> updateUser(Long userId, AppUser appUser) {
        return new ResponseEntity<>(userService.updateUser(userId, appUser), HttpStatus.OK);
    }
}
