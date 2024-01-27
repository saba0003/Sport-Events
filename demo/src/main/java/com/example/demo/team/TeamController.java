package com.example.demo.team;

import com.example.demo.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Team> listTeams() {
        return teamService.getTeams();
    }

    @GetMapping(path = "{teamId}")
    public Team getSpecificTeam(@PathVariable Long teamId) {
        return teamService.getTeamById(teamId);
    }

    @PostMapping
    public void registerNewTeam(@RequestBody Team team) {
        teamService.addNewTeam(team);
    }

    @DeleteMapping(path = "{teamId}")
    public void deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
    }

    @PutMapping(path = "{teamId}")
    public void updateTeam(@PathVariable Long teamId,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String city,
                           @RequestParam(required = false) List<Player> players) {
        teamService.updateTeam(teamId, name, city, players);
    }
}
