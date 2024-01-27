package com.example.demo.appuser;

import com.example.demo.event.Event;
import com.example.demo.player.Player;
import com.example.demo.util.Role;
import com.example.demo.util.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "spectators")
public class Spectator extends User {

    public Spectator(String username, String password, Role role) {
        super(username, password, role);
    }

//    public List<Player> viewPlayers() {
//
//    }
//
//    public String viewScore() {
//
//    }
//
//    public List<Event> viewEvents() {
//
//    }
}
