package com.example.demo.event;

import com.example.demo.team.Team;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EventService eventService;

    private Team barca, real;
    private Team milan, juventus;
    private Event uefaChampions, uefaEU;

    @BeforeEach
    void setUp() {
        // Given
        barca = new Team("Barcelona FC", "Barcelona");
        real = new Team("Real Madrid", "Madrid");
        uefaChampions = new Event("UEFA Champions League", barca, real);
        milan = new Team("AC Milan", "Milan");
        juventus = new Team("Juventus FC", "Turin");
        uefaEU = new Event("UEFA Europe League", milan, juventus);
    }

    @Test
    void listEventsTest() throws Exception {
        // Given
        List<Event> events = List.of(uefaChampions, uefaEU);

        // When
        when(eventService.getEvents()).thenReturn(events);

        ResultActions response = mockMvc.perform(get("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(events.size())))
                .andExpect(jsonPath("$[0].title", CoreMatchers.is(uefaChampions.getTitle())))
                .andExpect(jsonPath("$[0].team1.name", CoreMatchers.is(barca.getName())))
                .andExpect(jsonPath("$[0].team1.city", CoreMatchers.is(barca.getCity())))
                .andExpect(jsonPath("$[0].team2.name", CoreMatchers.is(real.getName())))
                .andExpect(jsonPath("$[0].team2.city", CoreMatchers.is(real.getCity())))
                .andDo(print());
    }

    @Test
    void getSpecificEventTest() throws Exception {
        // When
        when(eventService.getEventById(1L)).thenReturn(uefaChampions);

        ResultActions response = mockMvc.perform(get("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uefaChampions)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(uefaChampions.getTitle())))
                .andExpect(jsonPath("$.team1.name", CoreMatchers.is(barca.getName())))
                .andExpect(jsonPath("$.team1.city", CoreMatchers.is(barca.getCity())))
                .andDo(print());
    }

    @Test
    void registerNewEventTest() throws Exception {
        // Given
        given(eventService.addNewEvent(ArgumentMatchers.any(Event.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/events/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uefaChampions)));

        // Then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", CoreMatchers.is(uefaChampions.getTitle())))
                .andExpect(jsonPath("$.team1.name", CoreMatchers.is(barca.getName())))
                .andExpect(jsonPath("$.team1.city", CoreMatchers.is(barca.getCity())))
                .andDo(print());
    }

    @Test
    void deleteEventTest() throws Exception {
        // When
        doNothing().when(eventService).deleteEvent(1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/events/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(content().string("Event deleted!"));
    }

    @Test
    void updateEventTest() throws Exception {
        // Given
        ArgumentCaptor<Long> eventIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);

        // When
        when(eventService.updateEvent(eventIdCaptor.capture(), eventCaptor.capture())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Event event = invocation.getArgument(1);
            return id == 1L ? event : new IllegalArgumentException("Couldn't be updated!");
        });

        ResultActions response = mockMvc.perform(put("/api/v1/events/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uefaChampions)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(uefaChampions.getTitle())))
                .andExpect(jsonPath("$.team1.name", CoreMatchers.is(barca.getName())))
                .andExpect(jsonPath("$.team1.city", CoreMatchers.is(barca.getCity())))
                .andDo(print());
    }
}
