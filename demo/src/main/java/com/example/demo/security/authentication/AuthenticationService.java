package com.example.demo.security.authentication;

import com.example.demo.models.AppUser;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.models.spectator.Spectator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(AppUserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;

    }

    public ResponseEntity<String> registerUser(Map<String, String> userData) {
        if (userData == null) {
            logger.error("Invalid user!");
            return new ResponseEntity<>("Invalid user!", HttpStatus.BAD_REQUEST);
        }
        if (userData.size() != 2 || !userData.containsKey("username") || !userData.containsKey("password")) {
            logger.error("Invalid user data!");
            return new ResponseEntity<>("User data must only contain 'username' and 'password' fields and those fields can't be empty!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(userData.get("username"))) {
            logger.warn("Username is already taken!");
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        String username = userData.get("username");
        String password = userData.get("password");

        if (username.trim().isEmpty() || password.isEmpty()) {
            logger.error("Username or password must not be empty!");
            return new ResponseEntity<>("Username or password is empty!", HttpStatus.BAD_REQUEST);
        }

        password = passwordEncoder.encode(password);

        AppUser user = new Spectator(username, password);

        userRepository.save(user);

        logger.info("User successfully added!");

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    public ResponseEntity<String> loginUser(Map<String, String> credentials) {
        if (credentials == null) {
            logger.error("Invalid user!");
            return new ResponseEntity<>("Invalid user!", HttpStatus.BAD_REQUEST);
        }
        if (credentials.size() != 2 || !credentials.containsKey("username") || !credentials.containsKey("password")) {
            logger.error("Invalid user data!");
            return new ResponseEntity<>("User data must only contain 'username' and 'password' fields and those fields can't be empty!", HttpStatus.BAD_REQUEST);
        }

        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("User signed in successfully!");
            return new ResponseEntity<>("User signed in successfully!", HttpStatus.OK);
        } catch (AuthenticationException e) {
            logger.error("Invalid username or password!", e);
            return new ResponseEntity<>("Invalid username or password!", HttpStatus.UNAUTHORIZED);
        }
    }
}
