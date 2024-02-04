package com.example.demo.user;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRepository;
import com.example.demo.appuser.AppUserService;
import com.example.demo.appuser.admin.Admin;
import com.example.demo.appuser.coach.Coach;
import com.example.demo.appuser.spectator.Spectator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    private AppUserRepository userRepo;
    private AppUserService underTest;

    private Spectator spectator;
    private Coach coach;
    private Admin admin;

    @BeforeEach
    void setUp() {
        // Given
        underTest = new AppUserService(userRepo);
        spectator = new Spectator("EddieBrock", "venom");
        coach = new Coach("VinceLombardi", "CrazyFoot");
        admin = new Admin("AndyAnderson", "IHeardThat!");
    }

    @Test
    void getUserByIdIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getUserById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void getUserByIdIsIncorrectTest() {
        // When
        when(userRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> Optional.of(spectator);
                case 2 -> Optional.of(coach);
                case 3 -> Optional.of(admin);
                default -> Optional.empty();
            };
        });

        // Then
        assertThatThrownBy(() -> underTest.getUserById(4L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void getUserByIdTest() {
        // When
        when(userRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> Optional.of(spectator);
                case 2 -> Optional.of(coach);
                case 3 -> Optional.of(admin);
                default -> Optional.empty();
            };
        });
        AppUser result1 = underTest.getUserById(1L);
        AppUser result2 = underTest.getUserById(2L);
        AppUser result3 = underTest.getUserById(3L);

        // Then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1).isEqualTo(spectator);
        assertThat(result2).isEqualTo(coach);
        assertThat(result3).isEqualTo(admin);
    }

    @Test
    void getUserByUsernameIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getUserByUsername(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username!");
    }

    @Test
    void getUserByUsernameIsIncorrectTest() {
        // When
        when(userRepo.findByUsername(anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            return switch (username.trim()) {
                case "EddieBrock" -> Optional.of(spectator);
                case "VinceLombardi" -> Optional.of(coach);
                case "AndyAnderson" -> Optional.of(admin);
                default -> Optional.empty();
            };
        });

        // Then
        assertThatThrownBy(() -> underTest.getUserByUsername("Louie"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong username!");
    }

    @Test
    void getUserByUsernameTest() {
        // When
        when(userRepo.findByUsername(anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            return switch (username.trim()) {
                case "EddieBrock" -> Optional.of(spectator);
                case "VinceLombardi" -> Optional.of(coach);
                case "AndyAnderson" -> Optional.of(admin);
                default -> Optional.empty();
            };
        });
        AppUser result1 = underTest.getUserByUsername("EddieBrock");
        AppUser result2 = underTest.getUserByUsername("VinceLombardi");
        AppUser result3 = underTest.getUserByUsername("AndyAnderson");

        // Then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1).isEqualTo(spectator);
        assertThat(result2).isEqualTo(coach);
        assertThat(result3).isEqualTo(admin);
    }

    @Test
    void getAllUsersTest() {
        // When
        underTest.getUsers();

        // Then
        verify(userRepo).findAll();
    }

    @Test
    void addNewUserIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.addNewUser(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid user!");
    }

    @Test
    void addNewUserAlreadyExistsTest() {
        // Given
        spectator.setId(1L);

        // When
        when(userRepo.existsById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1, 2, 3 -> true;
                default -> false;
            };
        });
        AppUser result = underTest.addNewUser(spectator);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(spectator);
    }

    @Test
    void addNewUserTest() {
        // Given
        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);

        AppUser user = new Coach("LucianoSpalletti", "NapoliInItsPrime!");

        // When
        underTest.addNewUser(user);

        // Then
        verify(userRepo).save(captor.capture());

        assertThat(captor.getValue()).isEqualTo(user);
    }

    @Test
    void deleteUserWhenIdIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.deleteUser(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void deleteUserWhenIdIsIncorrectTest() {
        // When
        when(userRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> Optional.of(spectator);
                case 2 -> Optional.of(coach);
                case 3 -> Optional.of(admin);
                default -> Optional.empty();
            };
        });

        // Then
        assertThatThrownBy(() -> underTest.deleteUser(4L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void deleteUserTest() {
        // When
        when(userRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> Optional.of(spectator);
                case 2 -> Optional.of(coach);
                case 3 -> Optional.of(admin);
                default -> Optional.empty();
            };
        });
        underTest.deleteUser(1L);

        // Then
        verify(userRepo).deleteById(1L);
    }

    @Test
    void updateUserTest() {
        // Given
        AppUser newCoach = new Coach("LucianoSpalletti", "NapoliInItsPrime!");

        // When
        when(userRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> Optional.of(spectator);
                case 2 -> Optional.of(coach);
                case 3 -> Optional.of(admin);
                default -> Optional.empty();
            };
        });
        underTest.updateUser(1L, newCoach);

        // Then
        verify(userRepo).findById(1L);

        assertThat(spectator.getUsername()).isEqualTo(newCoach.getUsername());
        assertThat(spectator.getPassword()).isEqualTo(newCoach.getPassword());
        assertThat(spectator.getRole()).isEqualTo(newCoach.getRole());
    }
}
