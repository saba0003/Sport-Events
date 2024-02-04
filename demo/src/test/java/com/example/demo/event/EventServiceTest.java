package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepo;
    private EventService underTest;

    private Team barca, real;
    private Event uefa;

    @BeforeEach
    void setUp() {
        // Given
        underTest = new EventService(eventRepo);

        barca = new Team("Barcelona FC", "Barcelona");
        real = new Team("Real Madrid", "Madrid");
        uefa = new Event("UEFA Champions League", barca, real);
    }

    @Test
    void getEventByIdIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getEventById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void getEventByIdIsIncorrectTest() {
        // When
        when(eventRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.ofNullable((id == 1) ? uefa : null);
        });

        // Then
        assertThatThrownBy(() -> underTest.getEventById(2L)).
                isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void getEventByIdTest() {
        // When
        when(eventRepo.findById(1L)).thenReturn(Optional.of(uefa));

        Event result = underTest.getEventById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(uefa);
    }

    @Test
    void getEventByTitleIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getEventByTitle(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid title!");
    }

    @Test
    void getEventByTitleIsIncorrectTest() {
        // When
        when(eventRepo.findEventByTitle(anyString())).thenAnswer(invocation -> {
            String title = invocation.getArgument(0);
            return title.equals("UEFA Champions League") ? uefa : null;
        });

        // Then
        assertThatThrownBy(() -> underTest.getEventByTitle("Europe League"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong title!");
    }

    @Test
    void getEventByTitleTest() {
        // When
        when(eventRepo.findEventByTitle("UEFA Champions League")).thenReturn(uefa);

        Event result = underTest.getEventByTitle("UEFA Champions League");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(uefa);
    }

    @Test
    void getEventByParticipatingTeamIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getEventsByParticipatingTeam(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid team!");
    }

    @Test
    void getEventByParticipatingTeamIsIncorrectTest() {
        // Given
        Team napoli = new Team("Napoli", "Naples");

        // When
        when(eventRepo.findByParticipatingTeam(any(Team.class))).thenAnswer(invocation -> {
            Team team = invocation.getArgument(0);
            return (team.equals(barca) || team.equals(real)) ? uefa : null;
        });

        // Then
        assertThatThrownBy(() -> underTest.getEventsByParticipatingTeam(napoli))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong team!");
    }

    @Test
    void getEventByParticipatingTeamTest() {
        // When
        when(eventRepo.findByParticipatingTeam(any(Team.class))).thenAnswer(invocation -> {
            Team team = invocation.getArgument(0);
            return (team.equals(barca) || team.equals(real)) ?
                    Collections.singletonList(uefa) :
                    Collections.emptyList();
        });

        List<Event> result = underTest.getEventsByParticipatingTeam(uefa.getTeam1());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).contains(uefa);
    }

    @Test
    void getEventByStartingDateIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getEventsByStartingDate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid start date!");
    }

    @Test
    void getEventByStartingDateIsIncorrectTest() {
        // Given
        uefa.setStartDate(LocalDateTime.now().plusDays(1));

        // When
        when(eventRepo.findByStartingDate(any(LocalDate.class))).thenAnswer(invocation -> {
            LocalDate startDate = invocation.getArgument(0);
            return startDate.equals(LocalDate.now().plusDays(1)) ?
                    Collections.singletonList(uefa) :
                    Collections.emptyList();
        });

        List<Event> result = underTest.getEventsByStartingDate(LocalDate.now());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getEventByStartingDateTest() {
        // Given
        uefa.setStartDate(LocalDateTime.now().plusDays(1));

        // When
        when(eventRepo.findByStartingDate(LocalDate.now().plusDays(1))).thenReturn(List.of(uefa));

        List<Event> result = underTest.getEventsByStartingDate(LocalDate.now().plusDays(1));

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).contains(uefa);
    }

    @Test
    void getEventsByStatusIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.getEventsByStatus(null)).
                isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid status!");
    }

    @Test
    void getEventsByStatusIsIncorrectTest() {
        // Given
        uefa.setStatus(Status.UNSCHEDULED);

        // When
        when(eventRepo.findByStatus(any(Status.class))).thenAnswer(invocation -> {
            Status status = invocation.getArgument(0);
            return status.equals(Status.UNSCHEDULED) ?
                    Collections.singletonList(uefa) :
                    Collections.emptyList();
        });

        List<Event> result = underTest.getEventsByStatus(Status.SCHEDULED);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getEventsByStatusTest() {
        // Given
        uefa.setStatus(Status.UNSCHEDULED);

        // When
        when(eventRepo.findByStatus(any(Status.class))).thenAnswer(invocation -> {
            Status status = invocation.getArgument(0);
            return status.equals(Status.UNSCHEDULED) ?
                    Collections.singletonList(uefa) :
                    Collections.emptyList();
        });

        List<Event> result = underTest.getEventsByStatus(Status.UNSCHEDULED);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).contains(uefa);
    }

    @Test
    void getAllEventsTest() {
        // When
        underTest.getEvents();

        // Then
        verify(eventRepo).findAll();
    }

    @Test
    void addNewEventIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.addNewEvent(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid event!");
    }

    @Test
    void addNewEventAlreadyExistsTest() {
        // Given
        uefa.setId(1L);

        // When
        when(eventRepo.existsById(1L)).thenReturn(true);

        underTest.addNewEvent(uefa);

        // Then
        verify(eventRepo).existsById(1L);
    }

    @Test
    void addNewEventTest() {
        // When
        underTest.addNewEvent(uefa);

        // Then
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);

        verify(eventRepo).save(captor.capture());

        Event capturedEvent = captor.getValue();

        assertThat(capturedEvent).isEqualTo(uefa);
    }

    @Test
    void deleteEvenIsNullTest() {
        // When & Then
        assertThatThrownBy(() -> underTest.deleteEvent(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ID!");
    }

    @Test
    void deleteEventDoesNotExistTest() {
        // When
        when(eventRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L ? Optional.ofNullable(uefa) : Optional.empty();
        });

        // Then
        assertThatThrownBy(() -> underTest.deleteEvent(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong ID!");
    }

    @Test
    void deleteEventTest() {
        // When
        when(eventRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L ? Optional.ofNullable(uefa) : Optional.empty();
        });

        underTest.deleteEvent(1L);

        // Then
        verify(eventRepo).deleteById(1L);
    }

    @Test
    void updateEventTest() {
        // Given
        uefa.setStartDate(LocalDateTime.now().plusDays(2L));
        Team milan = new Team("AC Milan", "Milan");
        Team juventus = new Team("Juventus FC", "Turin");
        Event event = new Event("Europe League", milan, juventus);

        // When
        when(eventRepo.findById(1L)).thenReturn(Optional.ofNullable(uefa));

        Event updatedEvent = underTest.updateEvent(1L, event);

        // Then
        verify(eventRepo).findById(1L);

        assertThat(uefa.getTitle()).isEqualTo(updatedEvent.getTitle());
        assertThat(uefa.getTeam1()).isEqualTo(updatedEvent.getTeam1());
        assertThat(uefa.getTeam2()).isEqualTo(updatedEvent.getTeam2());
        assertThat(uefa.getStartDate()).isEqualTo(updatedEvent.getStartDate());
    }
}
