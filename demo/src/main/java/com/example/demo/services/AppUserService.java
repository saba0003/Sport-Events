package com.example.demo.services;

import com.example.demo.models.AppUser;

import java.util.List;

public interface AppUserService {
    AppUser getUserById(Long userId);
    AppUser getUserByUsername(String username);
    List<AppUser> getUsers();
    AppUser addNewUser(AppUser user);
    void deleteUser(Long userId);
    AppUser updateUser(Long userId, AppUser user);
}
