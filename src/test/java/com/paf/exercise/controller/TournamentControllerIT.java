package com.paf.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paf.exercise.model.Tournament;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TournamentControllerIT {

    public static final String API_TOURNAMENTS = "/api/tournaments";
    public static final String TOURNAMENTS_ID_URL_TEMPLATE = API_TOURNAMENTS + "/{id}";
    public static final String JSON_CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        Tournament tournament = Tournament.builder().id(1L).rewardAmount(200).build();
        mockMvc.perform(post(API_TOURNAMENTS)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @AfterEach
    public void cleanUp() {
        mockMvc.perform(delete(TOURNAMENTS_ID_URL_TEMPLATE, 1)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    void createTournament() {
        Tournament tournament = Tournament.builder().id(2L).rewardAmount(200).build();
        mockMvc.perform(post(API_TOURNAMENTS)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void createTournament_withEmptyContent() {
        mockMvc.perform(post(API_TOURNAMENTS)
                        .contentType(JSON_CONTENT_TYPE)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    void deleteTournament() {
        Tournament tournament = Tournament.builder().id(3L).rewardAmount(200).build();
        mockMvc.perform(post(API_TOURNAMENTS)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(TOURNAMENTS_ID_URL_TEMPLATE, 3)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getTournaments() {
        mockMvc.perform(get(API_TOURNAMENTS)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testGetTournament() {
        mockMvc.perform(get(TOURNAMENTS_ID_URL_TEMPLATE, 1)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getPlayers() {
        mockMvc.perform(get(TOURNAMENTS_ID_URL_TEMPLATE + "/players", 1)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update() {
        Tournament tournament = Tournament.builder().id(1L).rewardAmount(300).build();
        mockMvc.perform(put(TOURNAMENTS_ID_URL_TEMPLATE, 1)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());
    }
}