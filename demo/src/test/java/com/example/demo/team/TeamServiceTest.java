package com.example.demo.team;

import com.example.demo.models.Team;
import com.example.demo.models.Player;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.services.implementations.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepo;
    private TeamServiceImpl underTest;

    private Team napoli;

    @BeforeEach
    void setUp() {
        underTest = new TeamServiceImpl(teamRepo);
        // Given
        napoli = new Team("Napoli", "Naples");
    }

    @Test
    void getTeamByIdIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getTeamById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void getTeamByIdIsIncorrectTest() {
        // When
        when(teamRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long argument = invocation.getArgument(0);
            return argument.equals(1L) ? Optional.of(napoli) : Optional.empty();
        });

        // Then
        assertThatThrownBy(() -> underTest.getTeamById(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void getTeamByIdTest() {
        // When
        when(teamRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long argument = invocation.getArgument(0);
            return argument.equals(1L) ? Optional.of(napoli) : Optional.empty();
        });
        Team result = underTest.getTeamById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(napoli);
    }

    @Test
    void getTeamByNameIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getTeamByName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name!");
    }

    @Test
    void getTeamByNameIsIncorrectTest() {
        // When
        when(teamRepo.findByName(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Napoli") ? napoli : null;
        });

        // Then
        assertThatThrownBy(() -> underTest.getTeamByName("Inter"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong name!");
    }

    @Test
    void getTeamByNameTest() {
        // When
        when(teamRepo.findByName(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Napoli") ? napoli : null;
        });
        Team result = underTest.getTeamByName("Napoli");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(napoli);
    }

    @Test
    void getTeamByCityIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getTeamByCity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid city!");
    }

    @Test
    void getTeamByCityIsIncorrectTest() {
        // When
        when(teamRepo.findByCity(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Naples") ? List.of(napoli) : null;
        });

        // Then
        assertThatThrownBy(() -> underTest.getTeamByCity("Madrid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong city!");
    }

    @Test
    void getTeamByCityTest() {
        // When
        when(teamRepo.findByCity(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Naples") ? List.of(napoli) : null;
        });
        List<Team> teams = underTest.getTeamByCity("Naples");

        // Then
        assertThat(teams).isNotEmpty();
        assertThat(teams).contains(napoli);
    }

    @Test
    void getTeamByNumberOfPlayersIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getTeamsByNumberOfPlayers(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
    }

    @Test
    void getTeamByNumberOfPlayersIsNegativeTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getTeamsByNumberOfPlayers(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
    }

    @Test
    void getTeamByNumberOfPlayersIsIncorrectTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(teamRepo.findByNumberOfPlayers(anyInt())).thenAnswer(invocation -> {
            Integer argument = invocation.getArgument(0);
            return argument.equals(1) ? List.of(napoli) : new ArrayList<>();
        });

        // Then
        assertThatThrownBy(() -> underTest.getTeamsByNumberOfPlayers(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong number!");
    }

    @Test
    void getTeamByNumberOfPlayersTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(teamRepo.findByNumberOfPlayers(anyInt())).thenAnswer(invocation -> {
            Integer argument = invocation.getArgument(0);
            return argument.equals(1) ? List.of(napoli) : new ArrayList<>();
        });
        List<Team> teams = underTest.getTeamsByNumberOfPlayers(1);

        // Then
        assertThat(teams).isNotEmpty();
        assertThat(teams).contains(napoli);
    }

    @Test
    void getAllTeamsTest() {
        // When
        underTest.getTeams();

        // Then
        verify(teamRepo).findAll();
    }

    @Test
    void addNewTeamIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.addNewTeam(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid team!");
    }

    @Test
    void addNewTeamAlreadyExistsTest() {
        // Given
        napoli.setId(1L);

        // When
        when(teamRepo.existsById(1L)).thenReturn(true);

        underTest.addNewTeam(napoli);

        // Then
        verify(teamRepo).existsById(1L);
    }

    @Test
    void addNewTeamTest() {
        // When
        underTest.addNewTeam(napoli);

        // Then
        ArgumentCaptor<Team> captor = ArgumentCaptor.forClass(Team.class);

        verify(teamRepo).save(captor.capture());

        Team capturedTeam = captor.getValue();

        assertThat(capturedTeam).isEqualTo(napoli);
    }

    @Test
    void deleteTeamWhenTeamIdNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.deleteTeam(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void deleteTeamWhenTeamIdIsIncorrectTest() {
        // When
        when(teamRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long argument = invocation.getArgument(0);
            return argument.equals(1L) ? Optional.of(napoli) : Optional.empty();
        });

        // Then
        assertThatThrownBy(() -> underTest.deleteTeam(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void deleteTeamTest() {
        // When
        when(teamRepo.findById(1L)).thenReturn(Optional.of(napoli));
        underTest.deleteTeam(1L);

        // Then
        verify(teamRepo).deleteById(1L);
    }

    @Test
    void updateTeamTest() {
        // Given
        Long teamId = 1L;
        String newName = "Barcelona FC";
        String newCity = "Barcelona";
        List<Player> newPlayersList = new ArrayList<>();
        Team team = new Team(newName, newCity, newPlayersList);

        // When
        when(teamRepo.findById(1L)).thenReturn(Optional.of(napoli));

        underTest.updateTeam(teamId, team);

        // Then
        verify(teamRepo).findById(1L);

        assertThat(napoli.getName()).isEqualTo(newName);
        assertThat(napoli.getCity()).isEqualTo(newCity);
        assertThat(napoli.getPlayers()).isEqualTo(newPlayersList);
    }
}
