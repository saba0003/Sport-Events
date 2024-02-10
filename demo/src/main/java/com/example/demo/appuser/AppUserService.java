package com.example.demo.appuser;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private static final Logger logger = LogManager.getLogger(AppUserService.class);

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser getUserById(Long userId) {
        if (userId == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (userRepository.findById(userId).isEmpty()) {
            logger.warn(String.format("User with ID %d doesn't exist!", userId));
            throw new IllegalArgumentException("Wrong ID!");
        }
        return userRepository.findById(userId).get();
    }

    public AppUser getUserByUsername(String username) {
        if (username == null) {
            logger.error("Invalid username!");
            throw new IllegalArgumentException("Invalid username!");
        }
        if (userRepository.findByUsername(username).isEmpty()) {
            logger.warn(String.format("User with username %s doesn't exist!", username));
            throw new IllegalArgumentException("Wrong username!");
        }
        return userRepository.findByUsername(username).get();
    }

    public List<AppUser> getUsers() {
        List<AppUser> users = userRepository.findAll();
        if (users.isEmpty())
            logger.info("No user found!");
        return users;
    }

    public AppUser addNewUser(AppUser user) {
        if (user == null) {
            logger.error("Invalid user!");
            throw new IllegalArgumentException("Invalid user!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Username is already taken!");
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (userRepository.existsById(user.getId())) {
            logger.warn("User with this ID already exists!");
            return user;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User successfully added!");
        return user;
    }

    public void deleteUser(Long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
        logger.info("User deleted!");
    }

    @Transactional
    public AppUser updateUser(Long userId, AppUser user) {
        AppUser actual = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User couldn't be found!"));

        if (actual.equals(user)) {
            logger.info("User already exists!");
            return actual;
        }

        actual.setUsername(user.getUsername());
//        actual.setPassword(passwordEncoder.encode(user.getPassword()));
//        for some reason, encrypted passwords were differing.
        if (!user.getPassword().startsWith("$2a$"))
            actual.setPassword(passwordEncoder.encode(user.getPassword()));
        else
            actual.setPassword(user.getPassword());
        actual.setRole(user.getRole());

        logger.info("User successfully updated!");

        return actual;
    }
}
