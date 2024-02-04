package com.example.demo.appuser.coach;

import com.example.demo.appuser.AppUser;
import com.example.demo.util.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "coaches")
@PrimaryKeyJoinColumn(name = "app_user_id")
public class Coach extends AppUser {

    public Coach() {
        super.role = Role.COACH;
    }

    public Coach(String username, String password) {
        super(username, password);
        super.role = Role.COACH;
    }
}
