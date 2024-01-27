package com.example.demo.team;

import com.example.demo.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepo;

    private Team napoli, barca, real, manchesterCity, manchesterUnited;

    @BeforeEach
    void setUp() {
        // Given
        napoli = new Team("Napoli", "Naples");
        barca = new Team("Barcelona FC", "Barcelona");
        real = new Team("Real Madrid", "Madrid");
        manchesterCity = new Team("Manchester City", "Manchester");
        manchesterUnited = new Team("Manchester United", "Manchester");
        Player kvara = new Player("Khvicha", "Kvaratskhelia", napoli, 77);
        Player osimhen = new Player("Victor", "Osimhen", napoli, 9);
        Player politano = new Player("Matteo", "Politano", napoli, 21);
        Player messi = new Player("Lionel", "Messi", barca, 19);
        Player ronaldinho = new Player("Ronaldinho", "Gaúcho", barca, 10);
        Player etoo = new Player("Samuel", "Eto'o", barca, 9);
        napoli.setPlayers(List.of(kvara, osimhen, politano));
        barca.setPlayers(List.of(messi, ronaldinho, etoo));
        teamRepo.saveAll(List.of(napoli, barca, real, manchesterCity, manchesterUnited));
    }

    @AfterEach
    void tearDown() {
        teamRepo.deleteAll();
    }

    @Test
    void tryingToInitializeTeamWithNullNameOrCityTest() {
        // When & Then
        assertThatThrownBy(() -> new Team(null, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Name or city can't be null!");
        assertThatThrownBy(() -> new Team("Napoli", null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Name or city can't be null!");
        assertThatThrownBy(() -> new Team(null, "Naples")).isInstanceOf(IllegalArgumentException.class).hasMessage("Name or city can't be null!");
    }

    @Test
    void findByNameWhenNameIsNullTest() {
        // When
        Team savedTeam = teamRepo.findByName(null);

        // Then
        assertThat(savedTeam).isNull();
    }

    @Test
    void findByNameWhenNameIsIncorrectTest() {
        // When
        Team savedTeam = teamRepo.findByName("Inter");

        // Then
        assertThat(savedTeam).isNull();
    }

    @Test
    void findByNameTest() {
        // When
        Team savedTeam = teamRepo.findByName("Napoli");

        // Then
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam).isEqualTo(napoli);
    }

    @Test
    void findByCityWhenCityIsNullTest() {
        // When
        List<Team> teams = teamRepo.findByCity(null);

        // Then
        assertThat(teams).isEmpty();
    }

    @Test
    void findByCityWhenCityIsIncorrectTest() {
        // When
        List<Team> teams = teamRepo.findByCity("Milan");

        // Then
        assertThat(teams).isEmpty();
    }

    @Test
    void findByCityTest() {
        // When
        List<Team> teams = teamRepo.findByCity("Manchester");

        // Then
        assertThat(teams).isNotEmpty();
        assertThat(teams.size()).isEqualTo(2);
        assertThat(teams).contains(manchesterCity, manchesterUnited);
    }

    @Test
    void findByNumberOfPlayersWhenNumberOfPlayersIsNullTest() {
        // When
        List<Team> teams = teamRepo.findByNumberOfPlayers(null);

        // Then
        assertThat(teams).isEmpty();
    }

    @Test
    void findByNumberOfPlayersWhenNumberOfPlayersIsIncorrectTest() {
        // When
        List<Team> teams = teamRepo.findByNumberOfPlayers(2);

        // Then
        assertThat(teams).isEmpty();
    }

    @Test
    void findByNumberOfPlayersTest() {
        // When
        List<Team> teams = teamRepo.findByNumberOfPlayers(3);

        // Then
        assertThat(teams).isNotEmpty();
        assertThat(teams.size()).isEqualTo(2);
        assertThat(teams).contains(napoli, barca);
    }
}
