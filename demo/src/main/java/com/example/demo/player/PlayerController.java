package com.example.demo.player;

import com.example.demo.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "{playerId}")
    public Player getSpecificPlayer(@PathVariable Long playerId) {
        return playerService.getPlayerById(playerId);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewPlayer(@RequestBody Player player) {
        playerService.addNewPlayer(player);
    }

    @DeleteMapping(path = "{playerId}/delete")
    public void deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayer(playerId);
    }

//    @PutMapping(path = "{playerId}/update")
//    public Player updatePlayer(@PathVariable Long playerId,
//                             @RequestParam(required = false) String firstName,
//                             @RequestParam(required = false) String lastname,
//                             @RequestParam(required = false) Team team,
//                             @RequestParam(required = false) Integer jerseyNumber) {
//        return playerService.updatePlayer(playerId, firstName, lastname, team, jerseyNumber);
//    }

    @PutMapping(path = "{playerId}/update")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long playerId,
                                               @RequestBody Player player) {
        player.setId(playerId);
        Player response = playerService.updatePlayer(
                playerId,
                player.getFirstName(),
                player.getLastName(),
                player.getTeam(),
                player.getJerseyNumber());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
