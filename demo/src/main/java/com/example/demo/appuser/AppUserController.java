package com.example.demo.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class AppUserController {

    private final AppUserService userService;

    @Autowired
    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> listUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<AppUser> getSpecificUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppUser> registerNewUser(@RequestBody AppUser user) {
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{userId}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "{userId}/update")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long userId,
                                              @RequestBody AppUser user) {
        AppUser response = userService.updateUser(userId, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
