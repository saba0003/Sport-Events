package com.example.demo.models.admin;

import com.example.demo.models.AppUser;
import com.example.demo.util.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "app_user_id")
public class Admin extends AppUser {

    public Admin() {
        super.role = Role.ADMIN;
    }

    public Admin(String username, String password) {
        super(username, password);
        super.role = Role.ADMIN;
    }
}
