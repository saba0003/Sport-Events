package com.example.demo.controllers;

import com.example.demo.models.Player;
import com.example.demo.services.implementations.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/players")
public class PlayerController {

    private final PlayerServiceImpl playerService;

    @Autowired
    public PlayerController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SPECTATOR', 'COACH', 'ADMIN')")
    public ResponseEntity<List<Player>> listPlayers() {
        return new ResponseEntity<>(playerService.getPlayers(), HttpStatus.OK);
    }

    @GetMapping(path = "{playerId}")
    @PreAuthorize("hasAnyAuthority('SPECTATOR', 'COACH', 'ADMIN')")
    public ResponseEntity<Player> getSpecificPlayer(@PathVariable Long playerId) {
        return new ResponseEntity<>(playerService.getPlayerById(playerId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Player> registerNewPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.addNewPlayer(player), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{playerId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>("Player deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{playerId}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long playerId,
                                               @RequestBody Player player) {
        Player response = playerService.updatePlayer(playerId, player);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
