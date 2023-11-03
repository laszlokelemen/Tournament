package com.paf.exercise.service.impl;

import com.paf.exercise.exception.InvalidInputException;
import com.paf.exercise.exception.ResourceNotFoundException;
import com.paf.exercise.model.Player;
import com.paf.exercise.repository.PlayerRepository;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {
    @Mock
    private PlayerRepository playerRepositoryMock;

    @Mock
    private TournamentRepository tournamentRepositoryMock;

    @InjectMocks
    @Spy
    private PlayerServiceImpl playerServiceSpy;

    @Test
    public void testCreate() {
        Player player = getPlayer();

        when(tournamentRepositoryMock.existsById(any())).thenReturn(TRUE);
        when(playerRepositoryMock.existsById(any())).thenReturn(FALSE);
        when(playerRepositoryMock.save(any())).thenReturn(player);

        Player player1 = playerServiceSpy.create(1L, player);

        assertThat(player1).isEqualTo(player);
        verify(tournamentRepositoryMock, times(1)).existsById(1L);
        verify(playerRepositoryMock, times(1)).existsById(1L);
        verify(playerRepositoryMock, times(1)).save(player);
    }

    @Test
    public void testCreate_withAlreadyExistingPlayer() {
        Player player = getPlayer();

        when(tournamentRepositoryMock.existsById(1L)).thenReturn(TRUE);
        when(playerRepositoryMock.existsById(any())).thenReturn(TRUE);
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> playerServiceSpy.create(1L, player))
                .withMessage("The player with id: 1 is already created!");
        verify(playerRepositoryMock, times(0)).save(any(Player.class));
    }

    @Test
    public void testCreate_withNotExistingTournament() {
        Player player = getPlayer();

        when(tournamentRepositoryMock.existsById(any())).thenReturn(FALSE);

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> playerServiceSpy.create(1L, player))
                .withMessage("The tournament with id: 1 does not exist!");
        verify(playerRepositoryMock, times(0)).save(any(Player.class));
    }


    @Test
    public void testUpdate() {
        Player player = getPlayer();
        when(playerRepositoryMock.findById(any())).thenReturn(Optional.of(player));
        when(playerRepositoryMock.save(player)).thenReturn(player);

        Player updatedPlayer = playerServiceSpy.update(1L, player);

        assertThat(updatedPlayer).isEqualTo(player);
        verify(playerRepositoryMock, times(1)).save(any(Player.class));
    }

    @Test
    public void testUpdate_withNotExistingId() {
        Player player = getPlayer();

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> playerServiceSpy.update(1L, player))
                .withMessage("The player with id: 1 does not exist!");
        verify(playerRepositoryMock, times(0)).save(any(Player.class));
    }

    @Test
    public void testDelete() {
        Player player = getPlayer();
        when(playerRepositoryMock.findById(any())).thenReturn(Optional.of(player));
        playerServiceSpy.delete(1L);

        verify(playerRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    public void testDelete_withNotExistingId() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> playerServiceSpy.delete(1L))
                .withMessage("The player with id: 1 does not exist!");
        verify(playerRepositoryMock, times(0)).deleteById(any());
    }

    @Test
    public void testGetPlayersByName() {
        Player player = getPlayer();

        when(playerRepositoryMock.findByName(any())).thenReturn(List.of(player));
        assertThat(playerServiceSpy.getPlayersByName("Tester")).isEqualTo(List.of(player));
    }

    @Test
    public void testGetPlayersByName_withNotExistingName() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> playerServiceSpy.getPlayersByName("tester"))
                .withMessage("The player does not exits with name: tester");
    }

    @Test
    public void testGetPlayersByTournamentId() {
        Player player = getPlayer();

        Player player1 = new Player();
        player1.setName("Tester1");
        player1.setId(2L);

        when(tournamentRepositoryMock.existsById(any())).thenReturn(TRUE);
        when(playerRepositoryMock.findByTournamentId(1L)).thenReturn(Arrays.asList(player, player1));

        List<Player> playersByTournamentId = playerServiceSpy.getPlayersByTournamentId(1L);

        assertThat(playersByTournamentId).isEqualTo(Arrays.asList(player, player1));
        assertThat(playersByTournamentId.size()).isEqualTo(2);
    }

    @Test
    public void testGetPlayersByTournamentId_withNotExistingKey() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> playerServiceSpy.getPlayersByTournamentId(1L))
                .withMessage("The tournament with id: 1 does not exist!");
    }

    private static Player getPlayer() {
        return Player.builder().name("Tester").id(1L).build();
    }
}