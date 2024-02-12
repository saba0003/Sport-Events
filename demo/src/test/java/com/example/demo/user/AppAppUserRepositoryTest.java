package com.example.demo.user;

import com.example.demo.repositories.AppUserRepository;
import com.example.demo.models.admin.Admin;
import com.example.demo.models.coach.Coach;
import com.example.demo.models.spectator.Spectator;
import com.example.demo.models.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class AppAppUserRepositoryTest {

    @Autowired
    private AppUserRepository userRepo;

    private Spectator spectator;
    private Coach coach;
    private Admin admin;

    @BeforeEach
    void setUp() {
        spectator = new Spectator("Eddie", "Brock");
        coach = new Coach("Vince", "Lombardi");
        admin = new Admin("Andy", "Anderson");
        userRepo.saveAll(List.of(spectator, coach, admin));
    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void tryingToInitializeAndSaveUsersTest() {
        // When
        Optional<AppUser> user1 = userRepo.findByUsername("Eddie");
        Optional<AppUser> user2 = userRepo.findByUsername("Vince");
        Optional<AppUser> user3 = userRepo.findByUsername("Andy");

        // Then
        assertThat(user1).isNotEmpty();
        assertThat(user2).isNotEmpty();
        assertThat(user3).isNotEmpty();

        assertThat(user1.get()).isEqualTo(spectator);
        assertThat(user2.get()).isEqualTo(coach);
        assertThat(user3.get()).isEqualTo(admin);
    }

    @Test
    void tryingToInitializeUserWhenUsernameOrPasswordIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> new Spectator(null, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Username or password can't be null!");
        assertThatThrownBy(() -> new Coach("Eddie", null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Username or password can't be null!");
        assertThatThrownBy(() -> new Admin(null, "Brock")).isInstanceOf(IllegalArgumentException.class).hasMessage("Username or password can't be null!");
    }

    @Test
    void findByUsernameIsNullTest() {
        // When
        Optional<AppUser> spectator = userRepo.findByUsername(null);

        // Then
        assertThat(spectator).isEmpty();
    }

    @Test
    void findByUsernameIsIncorrectTest() {
        // When
        Optional<AppUser> user = userRepo.findByUsername("Louie");

        // Then
        assertThat(user).isEmpty();
    }

    @Test
    void findByUsernameTest() {
        // When
        Optional<AppUser> user = userRepo.findByUsername("Eddie");

        // Then
        assertThat(user).isNotEmpty();
        assertThat(user.get().getUsername()).isEqualTo(spectator.getUsername());
    }

    @Test
    void existsByUsernameIsNullTest() {
        // When
        boolean exists = userRepo.existsByUsername(null);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByUsernameIsIncorrectTest() {
        // When
        boolean exists = userRepo.existsByUsername("Louie");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByUsernameTest() {
        // When
        boolean exists = userRepo.existsByUsername("Eddie");

        // Then
        assertThat(exists).isTrue();
    }
}
