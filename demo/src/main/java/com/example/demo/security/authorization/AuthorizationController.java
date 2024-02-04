package com.example.demo.security.authorization;

import com.example.demo.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/home")
public class AuthorizationController {

    private final AuthorizationService authService;

    @Autowired
    public AuthorizationController(AuthorizationService authService) {
        this.authService = authService;
    }

    @GetMapping(path = "players")
    public ResponseEntity<List<Player>> listPlayers() {
        return authService.viewPlayers();
    }

    @GetMapping(path = "players/{playerId}")
    public ResponseEntity<Player> getSpecificPlayer(@PathVariable Long playerId) {
        return authService.getPlayerById(playerId);
    }

    @PostMapping(path = "players/create")
    @PreAuthorize("hasRole('SPECTATOR')")
    public ResponseEntity<Player> addNewPlayer(@RequestBody Player player) {
        return authService.addNewPlayer(player);
    }

    @DeleteMapping(path = "players/{playerId}/delete")
    public ResponseEntity<String> deletePlayer(@PathVariable Long playerId) {
        return authService.deletePlayer(playerId);
    }

    @PutMapping(path = "players/{playerId}/update")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long playerId,
                                               @RequestBody Player player) {
        return authService.updatePlayer(playerId, player);
    }
}
