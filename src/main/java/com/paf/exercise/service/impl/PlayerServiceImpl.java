package com.paf.exercise.service.impl;

import com.paf.exercise.exception.ResourceNotFoundException;
import com.paf.exercise.model.Player;
import com.paf.exercise.repository.PlayerRepository;
import com.paf.exercise.repository.TournamentRepository;
import com.paf.exercise.service.api.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {
    protected static final String PLAYER_DOES_NOT_EXISTS_ERROR = "The player with id: %s does not exist!";
    private final PlayerRepository playerRepository;
    private final TournamentRepository tournamentRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    @Override
    public Player create(long tournamentId, Player player) {
        return tournamentRepository
                .findById(tournamentId)
                .map(tournament -> {
                    player.setTournament(tournament);
                    return playerRepository.save(player);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(TournamentServiceImpl.TOURNAMENT_NOT_FOUND_ERROR, tournamentId)));
    }

    @Transactional
    @Override
    public Player update(long playerId, Player player) {
        Player updatedPlayer = playerRepository.findById(playerId).map(participant -> {
            participant.setId(player.getId());
            participant.setName(player.getName());
            return playerRepository.save(participant);
        }).orElseThrow(() -> new ResourceNotFoundException(format(PLAYER_DOES_NOT_EXISTS_ERROR, playerId)));

        log.info(format("Player with id: %s has been updated.", playerId));
        return updatedPlayer;
    }

    @Transactional
    @Override
    public void delete(long playerId) {
        playerRepository.findById(playerId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PLAYER_DOES_NOT_EXISTS_ERROR, playerId)));

        playerRepository.deleteById(playerId);
        log.info(format("The player with id: %s has been deleted!", playerId));

    }

    @Transactional(readOnly = true)
    @Override
    public List<Player> getPlayersByName(String name) {
        final List<Player> players = playerRepository.findByName(name);
        if (players.isEmpty()) {
            throw new ResourceNotFoundException(String.format(PLAYER_DOES_NOT_EXISTS_ERROR, name));
        }
        return players;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Player> getPlayersByTournamentId(long tournamentId) {
        checkIfTournamentExits(tournamentId);
        return playerRepository.findByTournamentId(tournamentId);

    }

    private void checkIfTournamentExits(long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException(String.format(TournamentServiceImpl.TOURNAMENT_NOT_FOUND_ERROR, tournamentId));
        }
    }
}
