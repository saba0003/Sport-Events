package com.example.demo.player;

import com.example.demo.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepo;
    private PlayerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PlayerService(playerRepo);
    }

    @Test
    void getPlayerByIdWhenIdIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayerById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void getPlayerByIdWithIncorrectIdTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia");

        // When
        when(playerRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long argument = invocation.getArgument(0);
            return argument.equals(1L) ? Optional.of(player) : Optional.empty();
        });

        // Then
        assertThatThrownBy(() -> underTest.getPlayerById(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void getPlayerByIdTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia");

        // When
        when(playerRepo.findById(1L)).thenReturn(Optional.of(player));
        Player result = underTest.getPlayerById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(player);
    }

    @Test
    void getPlayersByFirstNameWhenFirstNameIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayersByFirstName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid firstname!");
    }

    @Test
    void getPlayersByFirstNameWhenFirstNameIsIncorrectTest() {
        // Given
        Player player1 = new Player("Khvicha", "Kvarackhelia");
        Player player2 = new Player("Khvicha", "Managadze");
        Player player3 = new Player("Khvicha", "Sulaberidze");

        // When
        when(playerRepo.findByFirstName(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Khvicha") ? Arrays.asList(player1, player2, player3) : new ArrayList<>();
        });
        List<Player> players = underTest.getPlayersByFirstName("Someone");

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void getPlayersByFirstNameTest() {
        // Given
        Player player1 = new Player("Khvicha", "Kvarackhelia");
        Player player2 = new Player("Khvicha", "Managadze");
        Player player3 = new Player("Khvicha", "Sulaberidze");

        // When
        when(playerRepo.findByFirstName("Khvicha")).thenReturn(Arrays.asList(player1, player2, player3));
        List<Player> players = underTest.getPlayersByFirstName("Khvicha");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(3);
        assertThat(players.get(0)).isEqualTo(player1);
        assertThat(players.get(1)).isEqualTo(player2);
        assertThat(players.get(2)).isEqualTo(player3);
    }

    @Test
    void getPlayersByLastNameWhenLastNameIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayersByLastName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid lastname!");
    }

    @Test
    void getPlayersByLastNameWhenLastNameIsIncorrectTest() {
        // Given
        Player player1 = new Player("Giorgi", "Mikautadze");
        Player player2 = new Player("Dato", "Mikautadze");
        Player player3 = new Player("Saba", "Mikautadze");

        // When
        when(playerRepo.findByLastName(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Mikautadze") ? Arrays.asList(player1, player2, player3) : new ArrayList<>();
        });
        List<Player> players = underTest.getPlayersByLastName("Someone");

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void getPlayersByLastNameTest() {
        // Given
        Player player1 = new Player("Giorgi", "Mikautadze");
        Player player2 = new Player("Dato", "Mikautadze");
        Player player3 = new Player("Saba", "Mikautadze");

        // When
        when(playerRepo.findByLastName("Khvicha")).thenReturn(Arrays.asList(player1, player2, player3));
        List<Player> players = underTest.getPlayersByLastName("Khvicha");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(3);
        assertThat(players.get(0)).isEqualTo(player1);
        assertThat(players.get(1)).isEqualTo(player2);
        assertThat(players.get(2)).isEqualTo(player3);
    }

    @Test
    void getPlayersByTeamWhenTeamIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayersByTeam(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid team!");
    }

    @Test
    void getPlayersByTeamWhenIncorrectTeamTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeam(any())).thenAnswer(invocation -> {
            Team argument = invocation.getArgument(0);
            return argument.equals(napoli) ? List.of(player) : new ArrayList<>();
        });
        List<Player> players = underTest.getPlayersByTeam(barca);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void getPlayersByTeamTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeam(napoli)).thenReturn(List.of(player));
        List<Player> players = underTest.getPlayersByTeam(napoli);

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0)).isEqualTo(player);
    }

    @Test
    void getPlayersByTeamNameWhenTeamNameIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayersByTeamName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name!");
    }

    @Test
    void getPlayersByTeamNameWhenIncorrectTeamNameTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeamName(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Napoli") ? List.of(player) : new ArrayList<>();
        });
        List<Player> players = underTest.getPlayersByTeamName("Barcelona FC");

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void getPlayersByTeamNameTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeamName("Napoli")).thenReturn(List.of(player));
        List<Player> players = underTest.getPlayersByTeamName("Napoli");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0)).isEqualTo(player);
    }

    @Test
    void getPlayersByCityWhenCityIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayersByCity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid city!");
    }

    @Test
    void getPlayersByCityWhenIncorrectCityTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeamCity(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Naples") ? List.of(player) : new ArrayList<>();
        });
        List<Player> players = underTest.getPlayersByCity("Barcelona");

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void getPlayersByCityTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeamCity("Naples")).thenReturn(List.of(player));
        List<Player> players = underTest.getPlayersByCity("Naples");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0)).isEqualTo(player);
    }

    @Test
    void getPlayersByNumberWhenNumberIsNullOrIllegalTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayersByNumber(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
        assertThatThrownBy(() -> underTest.getPlayersByNumber(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
        assertThatThrownBy(() -> underTest.getPlayersByNumber(-3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
    }

    @Test
    void getPlayersByNumberWhenIncorrectNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByJerseyNumber(anyInt())).thenAnswer(invocation -> {
            Integer argument = invocation.getArgument(0);
            return argument.equals(77) ? List.of(player) : new ArrayList<>();
        });
        List<Player> players = underTest.getPlayersByNumber(10);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void getPlayersByNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByJerseyNumber(77)).thenReturn(List.of(player));
        List<Player> players = underTest.getPlayersByNumber(77);

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.get(0)).isEqualTo(player);
    }

    @Test
    void getPlayerByTeamAndNumberWhenTeamIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayerByTeamAndNumber(null, 77))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid team!");
    }

    @Test
    void getPlayerByTeamAndNumberWhenNumberIsNullOrIllegalTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayerByTeamAndNumber(new Team("Napoli", "Naples"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
        assertThatThrownBy(() -> underTest.getPlayerByTeamAndNumber(new Team("Napoli", "Naples"), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
        assertThatThrownBy(() -> underTest.getPlayerByTeamAndNumber(new Team("Napoli", "Naples"), -3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number!");
    }

    @Test
    void getPlayerByTeamAndNumberWhenTeamOrNumberIsIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeam(any())).thenAnswer(invocation -> {
            Team argument = invocation.getArgument(0);
            return argument.equals(napoli) ? List.of(player) : new ArrayList<>();
        });
        when(playerRepo.findByJerseyNumber(anyInt())).thenAnswer(invocation -> {
            Integer argument = invocation.getArgument(0);
            return argument.equals(77) ? List.of(player) : new ArrayList<>();
        });
        when(playerRepo.findByTeamAndJerseyNumber(any(), anyInt())).thenAnswer(invocation -> {
            Team argument1 = invocation.getArgument(0);
            Integer argument2 = invocation.getArgument(1);
            return argument1.equals(napoli) && argument2.equals(77) ? player : null;
        });

        Player result1 = underTest.getPlayerByTeamAndNumber(napoli, 78);
        Player result2 = underTest.getPlayerByTeamAndNumber(barca, 10);

        // Then
        assertThat(result1).isNull();
        assertThat(result2).isNull();
    }

    @Test
    void getPlayerByTeamAndNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeam(napoli)).thenReturn(List.of(player));
        when(playerRepo.findByJerseyNumber(77)).thenReturn(List.of(player));
        when(playerRepo.findByTeamAndJerseyNumber(napoli, 77)).thenReturn(player);
        Player result = underTest.getPlayerByTeamAndNumber(napoli, 77);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(player);
    }

    @Test
    void getPlayerByTeamNameAndNumberWhenTeamNameIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getPlayerByTeamNameAndNumber(null, 77))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid team name!");
    }

    @Test
    void getPlayerByTeamNameAndNumberWhenTeamNameOrNumberIsIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeamName(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            return argument.equals("Napoli") ? List.of(player) : new ArrayList<>();
        });
        when(playerRepo.findByJerseyNumber(anyInt())).thenAnswer(invocation -> {
            Integer argument = invocation.getArgument(0);
            return argument.equals(77) ? List.of(player) : new ArrayList<>();
        });
        when(playerRepo.findByTeamNameAndJerseyNumber(anyString(), anyInt())).thenAnswer(invocation -> {
            String argument1 = invocation.getArgument(0);
            Integer argument2 = invocation.getArgument(1);
            return argument1.equals("Napoli") && argument2.equals(77) ? player : null;
        });

        Player result1 = underTest.getPlayerByTeamNameAndNumber("Napoli", 78);
        Player result2 = underTest.getPlayerByTeamNameAndNumber("Barcelona FC", 10);

        // Then
        assertThat(result1).isNull();
        assertThat(result2).isNull();
    }

    @Test
    void getPlayerByTeamNameAndNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findByTeamName("Napoli")).thenReturn(List.of(player));
        when(playerRepo.findByJerseyNumber(77)).thenReturn(List.of(player));
        when(playerRepo.findByTeamNameAndJerseyNumber("Napoli", 77)).thenReturn(player);

        Player result = underTest.getPlayerByTeamNameAndNumber("Napoli", 77);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(player);
    }

    @Test
    void getAllPlayersTest() {
        // When
        underTest.getPlayers();

        // Then
        verify(playerRepo).findAll();
    }

    @Test
    void addNewPlayerWhenNewPlayerIsNullTest() {
        assertThatThrownBy(() -> underTest.addNewPlayer(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid player!");
    }

    @Test
    void addNewPlayerWhenPlayerAlreadyExistsTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        player.setId(1L);
        napoli.setPlayer(player);

        // When
        when(playerRepo.existsById(1L)).thenReturn(true);

        underTest.addNewPlayer(player);

        // Then
        verify(playerRepo).existsById(1L);
    }

    @Test
    void addNewPlayerTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        underTest.addNewPlayer(player);

        // Then
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

        verify(playerRepo).save(captor.capture());

        Player capturedPlayer = captor.getValue();

        assertThat(capturedPlayer).isEqualTo(player);
    }

    @Test
    void deletePlayerWhenPlayerIDIsNullTest() {
        assertThatThrownBy(() -> underTest.deletePlayer(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void deletePlayerWhenPlayerIDIsIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long argument = invocation.getArgument(0);
            return argument.equals(1L) ? Optional.of(player) : Optional.empty();
        });

        // Then
        assertThatThrownBy(() -> underTest.deletePlayer(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void deletePlayerTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        // When
        when(playerRepo.findById(1L)).thenReturn(Optional.of(player));
        underTest.deletePlayer(1L);

        // Then
        verify(playerRepo).deleteById(1L);
    }

    @Test
    void updatePlayerTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Long playerId = 1L;
        Player existingPlayer = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(existingPlayer);

        String newFirstName = "Lionel";
        String newLastName = "Messi";
        Integer newJerseyNumber = 10;

        // When
        when(playerRepo.findById(playerId)).thenReturn(Optional.of(existingPlayer));

        underTest.updatePlayer(playerId, newFirstName, newLastName, barca, newJerseyNumber);

        // Then
        verify(playerRepo, times(2)).findById(playerId);

        assertThat(existingPlayer.getFirstName()).isEqualTo(newFirstName);
        assertThat(existingPlayer.getLastName()).isEqualTo(newLastName);
        assertThat(existingPlayer.getTeam()).isEqualTo(barca);
        assertThat(existingPlayer.getJerseyNumber()).isEqualTo(newJerseyNumber);
    }
}
