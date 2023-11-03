package com.paf.exercise.service.impl;

import com.paf.exercise.exception.InvalidInputException;
import com.paf.exercise.exception.ResourceNotFoundException;
import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepositoryMock;

    @InjectMocks
    @Spy
    private TournamentServiceImpl tournamentServiceSpy;

    @Test
    void testCreate() {
        Tournament tournament = getTournament(1L, 200);
        when(tournamentRepositoryMock.save(any(Tournament.class))).thenReturn(tournament);
        Tournament tournament1 = tournamentServiceSpy.create(tournament);

        assertThat(tournament1).isEqualTo(tournament);
        verify(tournamentRepositoryMock, times(1)).save(any(Tournament.class));
    }

    @Test
    void testCreate_withAlreadyExits() {
        Tournament tournament = getTournament(1L, 200);
        when(tournamentRepositoryMock.existsById(any())).thenReturn(true);
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> tournamentServiceSpy.create(tournament))
                .withMessage("The tournament is already created with id: 1!");
    }

    @Test
    void testGetTournaments() {
        Tournament tournament = getTournament(1L, 200);
        Tournament tournament1 = getTournament(2L, 250);

        List<Tournament> tournamentList = Arrays.asList(tournament, tournament1);
        when(tournamentRepositoryMock.findAll()).thenReturn(tournamentList);
        List<Tournament> tournaments = tournamentServiceSpy.getTournaments();

        assertThat(tournaments).containsExactlyInAnyOrder(tournament, tournament1);

        verify(tournamentRepositoryMock, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Tournament tournament = getTournament(1L, 200);
        Tournament tournament1 = getTournament(2L, 250);

        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentRepositoryMock.save(any(Tournament.class))).thenReturn(tournament1);

        Tournament updatedTournament = tournamentServiceSpy.update(1L, tournament1);

        assertThat(updatedTournament).isEqualTo(tournament1);

        verify(tournamentRepositoryMock, times(1)).findById(any());
        verify(tournamentRepositoryMock, times(1)).save(any(Tournament.class));
    }


    @Test
    void testUpdate_withNotExistingKey() {
        Tournament tournament = mock(Tournament.class);

        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tournamentServiceSpy.update(1L, tournament))
                .withMessage("The tournament with id: 1 does not exist!");

        verify(tournamentRepositoryMock, times(1)).findById(any());
        verify(tournamentRepositoryMock, times(0)).save(any(Tournament.class));
    }


    @Test
    void testGetTournamentById() {
        Tournament tournament = getTournament(1L, 200);

        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.of(tournament));
        Tournament tournamentById = tournamentServiceSpy.getTournamentById(1L);

        verify(tournamentRepositoryMock, times(1)).findById(any());
        assertThat(tournamentById).isEqualTo(tournament);
    }

    @Test
    void testGetTournamentById_withNotExistingKey() {
        Tournament tournament = mock(Tournament.class);

        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tournamentServiceSpy.update(1L, tournament))
                .withMessage("The tournament with id: 1 does not exist!");

        verify(tournamentRepositoryMock, times(1)).findById(any());
    }

    @Test
    void testDelete() {
        Tournament tournament = getTournament(1L, 200);
        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentServiceSpy.delete(1L);

        verify(tournamentRepositoryMock, times(1)).findById(any());
        verify(tournamentRepositoryMock, times(1)).deleteById(1L);
        verify(tournamentRepositoryMock, times(0)).deleteById(any());
    }

    @Test
    void testDelete_withNotExistingKey() {
        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tournamentServiceSpy.delete(1L))
                .withMessage("The tournament with id: 1 does not exist!");

        verify(tournamentRepositoryMock, times(0)).deleteById(any());
    }

    @Test
    void testGetPlayers() {
        Player player = mock(Player.class);
        Player player1 = mock(Player.class);

        Tournament tournament = getTournament(1L, 200);
        tournament.setPlayers(Arrays.asList(player, player1));

        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.of(tournament));

        List<Player> players = tournamentServiceSpy.getPlayers(tournament.getId());
        assertThat(players.size()).isEqualTo(2);
        assertThat(players).containsExactlyInAnyOrder(player, player1);
        verify(tournamentRepositoryMock, times(1)).findById(any());
    }

    @Test
    void testGetPlayers_withNotExistingKey() {
        when(tournamentRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tournamentServiceSpy.getPlayers(1L))
                .withMessage("The tournament with id: 1 does not exist!");
        verify(tournamentRepositoryMock, times(1)).findById(any());
    }

    private static Tournament getTournament(long id, int rewardAmount) {
        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setRewardAmount(rewardAmount);
        return tournament;
    }
}