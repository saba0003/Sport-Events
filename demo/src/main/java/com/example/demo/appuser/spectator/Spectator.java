package com.example.demo.appuser.spectator;

import com.example.demo.util.Role;
import com.example.demo.appuser.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "spectators")
@PrimaryKeyJoinColumn(name = "app_user_id")
public class Spectator extends AppUser {

    public Spectator() {
        super.role = Role.SPECTATOR;
    }

    public Spectator(String username, String password) {
        super(username, password);
        super.role = Role.SPECTATOR;
    }
}
