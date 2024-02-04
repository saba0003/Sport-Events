package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import com.example.demo.util.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepo;
    @Mock
    private TeamRepository teamRepo;
    private Team barca, real;
    private Event uefa;

    @BeforeEach
    void setUp() {
        // Given teams
        barca = new Team("Barcelona FC", "Barcelona");
        real = new Team("Real Madrid", "Madrid");

        // When
        when(teamRepo.saveAll(anyIterable())).thenAnswer(invocation -> {
            Iterable<Team> teams = invocation.getArgument(0);
            return StreamSupport.stream(teams.spliterator(), false)
                    .collect(Collectors.toList());
        });

        when(teamRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.ofNullable((id == 1) ? barca : (id == 2) ? real : null);
        });

        teamRepo.saveAll(List.of(barca, real));

        // Fetch barca and real from the repository to ensure they are managed. Using them directly fails the test.
        Team team1 = teamRepo.findById(1L).orElse(null);
        Team team2 = teamRepo.findById(2L).orElse(null);

        // Given event
        uefa = new Event("UEFA Champions League", team1, team2);
        eventRepo.save(uefa);
    }

    @AfterEach
    void tearDown() {
        eventRepo.deleteAll();
    }

    @Test
    void tryingToInitializeEventWithIllegalArgumentsTest() {
        // When & Then
        assertThatThrownBy(() -> new Event(null, barca, real)).isInstanceOf(IllegalArgumentException.class).hasMessage("Title or any participating teams can't be null!");
        assertThatThrownBy(() -> new Event("UEFA Champions League", null, real)).isInstanceOf(IllegalArgumentException.class).hasMessage("Title or any participating teams can't be null!");
        assertThatThrownBy(() -> new Event("UEFA Champions League", barca, real, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Start date can't be null!");
        assertThatThrownBy(() -> new Event("UEFA Champions League", barca, real, LocalDateTime.now().minusDays(1))).isInstanceOf(IllegalArgumentException.class).hasMessage("Start date must be in the future!");
    }

    @Test
    void eventInitializationTest() {
        // When
        Event result = eventRepo.findById(uefa.getId()).orElse(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(uefa);
    }

    @Test
    void findEventByTitleIsNullOrIncorrectTest() {
        // When
        Event result1 = eventRepo.findEventByTitle(null);
        Event result2 = eventRepo.findEventByTitle("Europe League");

        // Then
        assertThat(result1).isNull();
        assertThat(result2).isNull();
    }

    @Test
    void findEventByTitleTest() {
        // When
        Event result = eventRepo.findEventByTitle("UEFA Champions League");

        // Then
        assertThat(result).isEqualTo(uefa);
    }

    @Test
    void findByParticipatingTeamIsNullOrIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");
        napoli.setId(3L);

        // When
        when(teamRepo.save(napoli)).thenReturn(napoli);
        when(teamRepo.findById(3L)).thenReturn(Optional.of(napoli));

        teamRepo.save(napoli);

        Team managed = teamRepo.findById(3L).orElse(null);

        List<Event> eventList1 = eventRepo.findByParticipatingTeam(null);
        List<Event> eventList2 = eventRepo.findByParticipatingTeam(managed);

        // Then
        assertThat(eventList1).isEmpty();
        assertThat(eventList2).isEmpty();
    }

    @Test
    void findByParticipatingTeamTest() {
        // When
        List<Event> events = eventRepo.findByParticipatingTeam(barca);

        // Then
        assertThat(events).isNotEmpty();
        assertThat(events).contains(uefa);
    }

    @Test
    void findByStartingDateIsNullOrIncorrectTest() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        uefa.setStartDate(startDate);

        // When
        List<Event> eventList1 = eventRepo.findByStartingDate(null);
        List<Event> eventList2 = eventRepo.findByStartingDate(startDate.plusDays(1).toLocalDate());

        // Then
        assertThat(eventList1).isEmpty();
        assertThat(eventList2).isEmpty();
    }

    @Test
    void findByStartingDateTest() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        uefa.setStartDate(startDate);

        // When
        List<Event> events = eventRepo.findByStartingDate(startDate.toLocalDate());

        // Then
        assertThat(events).isNotEmpty();
        assertThat(events).contains(uefa);
    }

    @Test
    void findByStatusIsNullOrIncorrectTest() {
        // When
        List<Event> eventList1 = eventRepo.findByStatus(null);
        List<Event> eventList2 = eventRepo.findByStatus(Status.SCHEDULED);

        // Then
        assertThat(eventList1).isEmpty();
        assertThat(eventList2).isEmpty();
    }

    @Test
    void findByStatusTest() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        uefa.setStartDate(startDate);

        // When
        List<Event> events = eventRepo.findByStatus(Status.SCHEDULED);

        // Then
        assertThat(events).isNotEmpty();
        assertThat(events).contains(uefa);
    }
}
