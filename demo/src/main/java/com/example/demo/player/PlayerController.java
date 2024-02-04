package com.example.demo.player;

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
    public ResponseEntity<List<Player>> listPlayers() {
        return new ResponseEntity<>(playerService.getPlayers(), HttpStatus.OK);
    }

    @GetMapping(path = "{playerId}")
    public ResponseEntity<Player> getSpecificPlayer(@PathVariable Long playerId) {
        return new ResponseEntity<>(playerService.getPlayerById(playerId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Player> registerNewPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.addNewPlayer(player), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{playerId}/delete")
    public ResponseEntity<String> deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>("Player deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{playerId}/update")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long playerId,
                                               @RequestBody Player player) {
        Player response = playerService.updatePlayer(playerId, player);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
