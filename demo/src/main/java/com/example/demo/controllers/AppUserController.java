package com.example.demo.controllers;

import com.example.demo.models.AppUser;
import com.example.demo.services.implementations.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/appusers")
public class AppUserController {

    private final AppUserServiceImpl userService;

    @Autowired
    public AppUserController(AppUserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AppUser>> listUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(path = "{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUser> getSpecificUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUser> registerNewUser(@RequestBody AppUser user) {
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{userId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{userId}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long userId,
                                              @RequestBody AppUser user) {
        AppUser response = userService.updateUser(userId, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
