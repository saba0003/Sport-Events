package com.example.demo.player;

import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepo;
    @Autowired
    private TeamRepository teamRepo;

    @AfterEach
    void tearDown() {
        playerRepo.deleteAll();
    }

    @Test
    void tryingToInitializePlayerWithNullFirstNameOrLastNameTest() {
        // When & Then
        assertThatThrownBy(() -> new Player(null, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Firstname and lastname can't be null!");
        assertThatThrownBy(() -> new Player("Khvicha", null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Firstname and lastname can't be null!");
        assertThatThrownBy(() -> new Player(null, "Kvarackhelia")).isInstanceOf(IllegalArgumentException.class).hasMessage("Firstname and lastname can't be null!");
    }

    @Test
    void tryingToInitializePlayerWithTeamBeingNullTest() {
        // When & Then
        assertThatThrownBy(() -> new Player("Khvicha", "Kvarackhelia", null, 77)).isInstanceOf(IllegalArgumentException.class).hasMessage("If player has jersey number, he/she must be in a team!");
    }

    @Test
    void tryingToInitializePlayerWithJerseyNumberBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        // When & Then
        assertThatThrownBy(() -> new Player("Khvicha", "Kvarackhelia", napoli, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("When in team, jersey number can't be null!");
    }

    @Test
    void tryingToInitializePlayerWithJerseyNumberBeingBelowOrEqualToZeroTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        // When & Then
        assertThatThrownBy(() -> new Player("Khvicha", "Kvarackhelia", napoli, 0)).isInstanceOf(IllegalArgumentException.class).hasMessage("Player numbering begins from 1 upwards!");
        assertThatThrownBy(() -> new Player("Khvicha", "Kvarackhelia", napoli, -3)).isInstanceOf(IllegalArgumentException.class).hasMessage("Player numbering begins from 1 upwards!");
    }

    /**
     * Instantiating a player having team and jerseyNumber null is the same as calling the
     * second constructor for player with only 2 parameters required (firstName and lastName).
    **/
    @Test
    void tryingToInitializePlayerWithTeamAndJerseyNumberBeingNullTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia", null, null);
        playerRepo.save(player);

        // When
        Player savedPlayer = playerRepo.findById(player.getId()).orElse(null);

        // Then
        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getFullName()).isEqualTo(player.getFullName());
    }

    @Test
    void findByFirstNameOrLastNameBeingNullTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia");
        playerRepo.save(player);

        // When
        List<Player> playersByFirstName = playerRepo.findByFirstName(null);
        List<Player> playersByLastName = playerRepo.findByLastName(null);

        // Then
        assertThat(playersByFirstName.isEmpty()).isTrue();
        assertThat(playersByLastName.isEmpty()).isTrue();
    }

    @Test
    void findByFirstNameOrLastNameBeingIncorrectTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia");
        playerRepo.save(player);

        // When
        List<Player> playersByFirstName = playerRepo.findByFirstName("Giorgi");
        List<Player> playersByLastName = playerRepo.findByLastName("Dato");

        // Then
        assertThat(playersByFirstName.isEmpty()).isTrue();
        assertThat(playersByLastName.isEmpty()).isTrue();
    }

    @Test
    void findByFirstNameTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia");
        playerRepo.save(player);

        // When
        List<Player> players = playerRepo.findByFirstName(player.getFirstName());

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.get(0)).isEqualTo(player);
    }

    @Test
    void findByLastNameTest() {
        // Given
        Player player = new Player("Khvicha", "Kvarackhelia");
        playerRepo.save(player);

        // When
        List<Player> players = playerRepo.findByLastName(player.getLastName());

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.get(0)).isEqualTo(player);
    }

    @Test
    void findByFirstNameWithMultipleElementsWithTheSameValuesInTheListTest() {
        // Given
        Player player1 = new Player("Khvicha", "Kvarackhelia");
        Player player2 = new Player("Khvicha", "Managadze");
        Player player3 = new Player("Khvicha", "Sulaberidze");

        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);

        // When
        List<Player> players = playerRepo.findByFirstName("Khvicha");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(playerRepo.count());
        assertThat(players.get(0)).isEqualTo(player1);
        assertThat(players.get(1)).isEqualTo(player2);
        assertThat(players.get(2)).isEqualTo(player3);
    }

    @Test
    void findByLastNameWithMultipleElementsWithTheSameValuesInTheListTest() {
        // Given
        Player player1 = new Player("Giorgi", "Mikautadze");
        Player player2 = new Player("Dato", "Mikautadze");
        Player player3 = new Player("Saba", "Mikautadze");

        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);

        // When
        List<Player> players = playerRepo.findByLastName("Mikautadze");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players.size()).isEqualTo(playerRepo.count());
        for (Player player : players)
            assertThat(player.getLastName()).isEqualTo("Mikautadze");
    }

    @Test
    void findByTeamBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByTeam(null);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByTeamWithIncorrectValueTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);
        teamRepo.save(barca);

        // When
        List<Player> players = playerRepo.findByTeam(barca);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByTeamTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByTeam(napoli);

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players).isEqualTo(napoli.getPlayers());
    }

    @Test
    void findByTeamNameBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByTeamName(null);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByTeamNameWithIncorrectValueTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);
        teamRepo.save(barca);

        // When
        List<Player> players = playerRepo.findByTeamName("Barcelona");

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByTeamNameTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByTeamName("Napoli");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players).isEqualTo(napoli.getPlayers());
    }

    @Test
    void findByTeamCityBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByTeamCity(null);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByTeamCityWithIncorrectValueTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);
        teamRepo.save(barca);

        // When
        List<Player> players = playerRepo.findByTeamCity("Barcelona");

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByTeamCityTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByTeamCity("Italy");

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players).isEqualTo(napoli.getPlayers());
    }

    @Test
    void findByJerseyNumberBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByJerseyNumber(null);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByJerseyNumberWithIncorrectValueTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByJerseyNumber(78);

        // Then
        assertThat(players.isEmpty()).isTrue();
    }

    @Test
    void findByJerseyNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        napoli.setPlayer(player);

        playerRepo.save(player);
        teamRepo.save(napoli);

        // When
        List<Player> players = playerRepo.findByJerseyNumber(77);

        // Then
        assertThat(players.isEmpty()).isFalse();
        assertThat(players).isEqualTo(napoli.getPlayers());
    }

    @Test
    void findByTeamAndJerseyNumberWithEitherAttributeBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player1 = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        Player player2 = new Player("Victor", "Osimhen", napoli, 9);
        Player player3 = new Player("Matteo", "Politano", napoli, 21);

        napoli.setPlayers(Arrays.asList(player1, player2, player3));
        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);
        teamRepo.save(napoli);

        // When
        Player actual1 = playerRepo.findByTeamAndJerseyNumber(null, 77);
        Player actual2 = playerRepo.findByTeamAndJerseyNumber(napoli, null);
        Player actual3 = playerRepo.findByTeamAndJerseyNumber(null, null);

        // Then
        assertThat(actual1).isNull();
        assertThat(actual2).isNull();
        assertThat(actual3).isNull();
    }

    @Test
    void findByTeamAndJerseyNumberWithEitherAttributeBeingIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Team real = new Team("Real Madrid", "Madrid");
        Player player1 = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        Player player2 = new Player("Victor", "Osimhen", napoli, 9);
        Player player3 = new Player("Matteo", "Politano", napoli, 21);

        napoli.setPlayers(Arrays.asList(player1, player2, player3));
        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);
        teamRepo.save(napoli);
        teamRepo.save(barca);
        teamRepo.save(real);

        // When
        Player actual1 = playerRepo.findByTeamAndJerseyNumber(napoli, 12);
        Player actual2 = playerRepo.findByTeamAndJerseyNumber(barca, 10);
        Player actual3 = playerRepo.findByTeamAndJerseyNumber(real, 7);

        // Then
        assertThat(actual1).isNull();
        assertThat(actual2).isNull();
        assertThat(actual3).isNull();
    }

    @Test
    void findByTeamAndJerseyNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player1 = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        Player player2 = new Player("Victor", "Osimhen", napoli, 9);
        Player player3 = new Player("Matteo", "Politano", napoli, 21);

        napoli.setPlayers(Arrays.asList(player1, player2, player3));
        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);
        teamRepo.save(napoli);

        // When
        Player player = playerRepo.findByTeamAndJerseyNumber(napoli, 21);

        // Then
        assertThat(player).isNotNull();
        assertThat(player.getFullName()).isEqualTo(player3.getFullName());
        assertThat(player.getJerseyNumber()).isEqualTo(player3.getJerseyNumber());
    }

    @Test
    void findByTeamNameAndJerseyNumberWithEitherAttributeBeingNullTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player1 = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        Player player2 = new Player("Victor", "Osimhen", napoli, 9);
        Player player3 = new Player("Matteo", "Politano", napoli, 21);

        napoli.setPlayers(Arrays.asList(player1, player2, player3));
        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);
        teamRepo.save(napoli);

        // When
        Player actual1 = playerRepo.findByTeamNameAndJerseyNumber(null, 77);
        Player actual2 = playerRepo.findByTeamNameAndJerseyNumber("Napoli", null);
        Player actual3 = playerRepo.findByTeamNameAndJerseyNumber(null, null);

        // Then
        assertThat(actual1).isNull();
        assertThat(actual2).isNull();
        assertThat(actual3).isNull();
    }

    @Test
    void findByTeamNameAndJerseyNumberWithEitherAttributeBeingIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Team barca = new Team("Barcelona FC", "Barcelona");
        Team real = new Team("Real Madrid", "Madrid");
        Player player1 = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        Player player2 = new Player("Victor", "Osimhen", napoli, 9);
        Player player3 = new Player("Matteo", "Politano", napoli, 21);

        napoli.setPlayers(Arrays.asList(player1, player2, player3));
        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);
        teamRepo.save(napoli);
        teamRepo.save(barca);
        teamRepo.save(real);

        // When
        Player actual1 = playerRepo.findByTeamNameAndJerseyNumber("Napoli", 12);
        Player actual2 = playerRepo.findByTeamNameAndJerseyNumber("Barcelona FC", 10);
        Player actual3 = playerRepo.findByTeamNameAndJerseyNumber("Real Madrid", 7);

        // Then
        assertThat(actual1).isNull();
        assertThat(actual2).isNull();
        assertThat(actual3).isNull();
    }

    @Test
    void findByTeamNameAndJerseyNumberTest() {
        // Given
        Team napoli = new Team("Napoli", "Italy");
        Player player1 = new Player("Khvicha", "Kvarackhelia", napoli, 77);
        Player player2 = new Player("Victor", "Osimhen", napoli, 9);
        Player player3 = new Player("Matteo", "Politano", napoli, 21);

        napoli.setPlayers(Arrays.asList(player1, player2, player3));
        playerRepo.save(player1);
        playerRepo.save(player2);
        playerRepo.save(player3);
        teamRepo.save(napoli);

        // When
        Player player = playerRepo.findByTeamNameAndJerseyNumber("Napoli", 9);

        // Then
        assertThat(player).isNotNull();
        assertThat(player.getFullName()).isEqualTo(player2.getFullName());
        assertThat(player.getJerseyNumber()).isEqualTo(player2.getJerseyNumber());
    }
}
