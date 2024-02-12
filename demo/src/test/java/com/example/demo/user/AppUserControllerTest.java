package com.example.demo.user;

import com.example.demo.models.AppUser;
import com.example.demo.controllers.AppUserController;
import com.example.demo.services.implementations.AppUserServiceImpl;
import com.example.demo.models.admin.Admin;
import com.example.demo.models.coach.Coach;
import com.example.demo.models.spectator.Spectator;
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
@WebMvcTest(controllers = AppUserController.class)
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AppUserServiceImpl userService;

    private Spectator spectator;
    private Coach coach;
    private Admin admin;

    @BeforeEach
    void setUp() {
        // Given
        spectator = new Spectator("EddieBrock", "venom");
        coach = new Coach("VinceLombardi", "CrazyFoot");
        admin = new Admin("AndyAnderson", "IHeardThat!");
    }

    @Test
    void listUsersTest() throws Exception {
        // Given
        List<AppUser> users = List.of(spectator, coach, admin);

        // When
        when(userService.getUsers()).thenReturn(users);

        ResultActions response = mockMvc.perform(get("/api/v1/appusers")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(users.size())))
                .andExpect(jsonPath("$[0].username", CoreMatchers.is(users.get(0).getUsername())))
                .andExpect(jsonPath("$[0].password", CoreMatchers.is(users.get(0).getPassword())))
                .andExpect(jsonPath("$[0].role", CoreMatchers.is(users.get(0).getRole().name())))
                .andDo(print());
    }

    @Test
    void getSpecificUserTest() throws Exception {
        // When
        when(userService.getUserById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> spectator;
                case 2 -> coach;
                case 3 -> admin;
                default -> null;
            };
        });

        ResultActions response = mockMvc.perform(get("/api/v1/appusers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spectator)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", CoreMatchers.is(spectator.getUsername())))
                .andExpect(jsonPath("$.password", CoreMatchers.is(spectator.getPassword())))
                .andExpect(jsonPath("$.role", CoreMatchers.is(spectator.getRole().name())))
                .andDo(print());
    }

    @Test
    void registerNewUserTest() throws Exception {
        // Given
        given(userService.addNewUser(ArgumentMatchers.any(AppUser.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        // The issue lies in the fact that GrantedAuthority is an interface,
        // and Jackson doesn't know how to construct instances of interfaces directly.
        ResultActions response = mockMvc.perform(post("/api/v1/appusers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spectator)));

        // Then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", CoreMatchers.is(spectator.getUsername())))
                .andExpect(jsonPath("$.password", CoreMatchers.is(spectator.getPassword())))
                .andExpect(jsonPath("$.role", CoreMatchers.is(spectator.getRole().name())))
                .andDo(print());
    }

    @Test
    void deleteUserTest() throws Exception {
        // When
        doNothing().when(userService).deleteUser(1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/appusers/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted!"));
    }

    @Test
    void updateUserTest() throws Exception {
        // Given
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);

        // When
        when(userService.updateUser(userIdCaptor.capture(), userCaptor.capture())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            AppUser user = invocation.getArgument(1);
            return id == 1L ? user : new IllegalArgumentException("Couldn't be updated!");
        });

        // The issue lies in the fact that GrantedAuthority is an interface,
        // and Jackson doesn't know how to construct instances of interfaces directly.
        ResultActions response = mockMvc.perform(put("/api/v1/appusers/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spectator)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", CoreMatchers.is(spectator.getUsername())))
                .andExpect(jsonPath("$.password", CoreMatchers.is(spectator.getPassword())))
                .andExpect(jsonPath("$.role", CoreMatchers.is(spectator.getRole().name())))
                .andDo(print());
    }
}
