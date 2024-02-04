package com.example.demo.team;

import com.example.demo.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<Team>> listTeams() {
        return new ResponseEntity<>(teamService.getTeams(), HttpStatus.OK);
    }

    @GetMapping(path = "{teamId}")
    public ResponseEntity<Team> getSpecificTeam(@PathVariable Long teamId) {
        return new ResponseEntity<>(teamService.getTeamById(teamId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Team> registerNewTeam(@RequestBody Team team) {
        return new ResponseEntity<>(teamService.addNewTeam(team), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{teamId}/delete")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return new ResponseEntity<>("Team deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{teamId}/update")
    public ResponseEntity<Team> updateTeam(@PathVariable Long teamId,
                                           @RequestBody Team team) {
        Team response = teamService.updateTeam(teamId, team);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
