package com.example.demo.player;

import com.example.demo.controllers.PlayerController;
import com.example.demo.models.Player;
import com.example.demo.services.implementations.PlayerServiceImpl;
import com.example.demo.models.Team;
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

@WebMvcTest(controllers = PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PlayerServiceImpl playerService;

    private Team napoli, barca, real;
    private Player kvara, osimhen, politano;
    private Player messi, ronaldinho, etoo;
    private Player ronaldo, benzema, bale;

    @BeforeEach
    void setUp() {
        // Given
        napoli = new Team("Napoli", "Naples");
        barca = new Team("Barcelona FC", "Barcelona");
        real = new Team("Real Madrid", "Madrid");
        kvara = new Player("Khvicha", "Kvaratskhelia", napoli, 77);
        osimhen = new Player("Victor", "Osimhen", napoli, 9);
        politano = new Player("Matteo", "Politano", napoli, 21);
        messi = new Player("Lionel", "Messi", barca, 19);
        ronaldinho = new Player("Ronaldinho", "Gaúcho", barca, 10);
        etoo = new Player("Samuel", "Eto'o", barca, 9);
        ronaldo = new Player("Cristiano", "Ronaldo", real, 7);
        benzema = new Player("Karim", "Benzema", real, 9);
        bale = new Player("Gareth", "Bale", real, 11);
        napoli.setPlayers(List.of(kvara, osimhen, politano));
        barca.setPlayers(List.of(messi, ronaldinho, etoo));
        real.setPlayers(List.of(ronaldo, benzema, bale));
    }

    @Test
    void listPlayersTest() throws Exception {
        // Given
        List<Player> players = List.of(kvara, osimhen, politano);

        // When
        when(playerService.getPlayers()).thenReturn(players);

        ResultActions response = mockMvc.perform(get("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(players.size())))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Khvicha")))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Kvaratskhelia")))
                .andExpect(jsonPath("$[0].team.city", CoreMatchers.is("Naples")))
                .andExpect(jsonPath("$[0].jerseyNumber", CoreMatchers.is(77)))
                .andDo(print());
    }

    @Test
    void getSpecificPlayerTest() throws Exception {
        // When
        when(playerService.getPlayerById(1L)).thenReturn(kvara);

        ResultActions response = mockMvc.perform(get("/api/v1/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kvara)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Khvicha")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Kvaratskhelia")))
                .andExpect(jsonPath("$.team.city", CoreMatchers.is("Naples")))
                .andExpect(jsonPath("$.jerseyNumber", CoreMatchers.is(77)))
                .andDo(print());
    }

    @Test
    void registerNewPlayerTest() throws Exception {
        // Given
        given(playerService.addNewPlayer(ArgumentMatchers.any(Player.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/players/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kvara)));

        // Then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(kvara.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(kvara.getLastName())))
                .andDo(print());
    }

    @Test
    void deletePlayerTest() throws Exception {
        // When
        doNothing().when(playerService).deletePlayer(1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/players/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(content().string("Player deleted!"));
    }

    @Test
    void updatePlayerTest() throws Exception {
        // Given
        ArgumentCaptor<Long> playerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);

        // When
        when(playerService.updatePlayer(playerIdCaptor.capture(), playerCaptor.capture())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Player player = invocation.getArgument(1);
            return id == 1 ? player : new IllegalArgumentException("Couldn't be updated!");
        });

        ResultActions response = mockMvc.perform(put("/api/v1/players/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kvara)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(kvara.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(kvara.getLastName())))
                .andExpect(jsonPath("$.team.city", CoreMatchers.is(kvara.getTeam().getCity())))
                .andExpect(jsonPath("$.jerseyNumber", CoreMatchers.is(kvara.getJerseyNumber())))
                .andDo(print());
    }
}
