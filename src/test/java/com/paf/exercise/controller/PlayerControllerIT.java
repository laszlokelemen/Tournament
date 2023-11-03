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
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;

import static com.paf.exercise.controller.TournamentControllerIT.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PlayerControllerIT {

    public static final String API_PLAYERS_URL_TEMPLATE = "/api/players";
    public static final String API_PLAYERS_ID_URL_TEMPLATE = API_PLAYERS_URL_TEMPLATE + "/{id}";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeEach
    public void testSetUp() {
        Tournament tournament = Tournament.builder().id(1L).rewardAmount(200).build();
        mockMvc.perform(post(API_TOURNAMENTS)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());

        mockMvc.perform(post(API_PLAYERS_URL_TEMPLATE)
                        .contentType("application/json")
                        .content("{\n" +
                                "    \"id\" : \"1\",\n" +
                                "    \"name\": \"Tester\",\n" +
                                "    \"tournament\":\n" +
                                "        {\n" +
                                "            \"id\" : \"1\"\n" +
                                "        }\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @AfterEach
    public void testCleanUp() {
        mockMvc.perform(delete(TOURNAMENTS_ID_URL_TEMPLATE, 1)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void createPlayer() {
        mockMvc.perform(post(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .content("{\n" +
                                "    \"name\": \"PLayer\",\n" +
                                "    \"tournament\":\n" +
                                "        {\n" +
                                "            \"id\" : \"1\"\n" +
                                "        }\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void testCreatePlayer_withEmptyContent() {
        mockMvc.perform(post(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class))
                .andExpect(result -> assertThat(requireNonNull(result.getResolvedException()).getMessage())
                        .contains("The name object is required!"))
                .andExpect(result -> assertThat(requireNonNull(result.getResolvedException()).getMessage())
                        .contains("The tournament object is required!"));
    }


    @SneakyThrows
    @Test
    public void testGetPlayerName() {
        mockMvc.perform(get(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .param("name", "Tester"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void testGetPlayerName_withEmptyNameParam() {
        mockMvc.perform(get(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .param("name", ""))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat(requireNonNull(result.getResolvedException()).getMessage())
                        .contains("The name parameter must not be blank!"));
    }

    @SneakyThrows
    @Test
    public void testUpdatePlayer() {
        mockMvc.perform(put(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .content("{\n" +
                                "    \"name\": \"Creator\",\n" +
                                "    \"tournament\":\n" +
                                "        {\n" +
                                "            \"id\" : \"1\"\n" +
                                "        }\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void testUpdatePlayer_withEmptyContent() {
        mockMvc.perform(put(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class))
                .andExpect(result -> assertThat(requireNonNull(result.getResolvedException()).getMessage())
                        .contains("The name object is required!"))
                .andExpect(result -> assertThat(requireNonNull(result.getResolvedException()).getMessage())
                        .contains("The tournament object is required!"));
    }

    @SneakyThrows
    @Test
    public void testGetPlayersByTournamentId() {
        mockMvc.perform(get(API_PLAYERS_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE)
                        .param("tournament_id", "1"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void testDeletePlayer() {
        mockMvc.perform(delete(API_PLAYERS_ID_URL_TEMPLATE, 1)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }
}
