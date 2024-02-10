package com.example.demo.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/users/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    @Autowired
    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> userData) {
        return authService.registerUser(userData);
    }

    @PostMapping(path = "login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        return authService.loginUser(credentials);
    }
}
