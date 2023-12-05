package com.example.demo.player;

import com.example.demo.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> listPlayers() {
        return playerService.getPlayers();
    }

    @PostMapping
    public void registerNewPlayer(@RequestBody Player player) {
        playerService.addNewPlayer(player);
    }

    @DeleteMapping(path = {"playerId"})
    public void deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayer(playerId);
    }

    @PutMapping(path = {"playerId"})
    public void updatePlayer(@PathVariable Long playerId,
                             @RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String lastname,
                             @RequestParam(required = false) Team team) {
        playerService.updatePlayer(playerId, firstName, lastname, team);
    }
}
