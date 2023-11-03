package com.paf.exercise.service.api;

import com.paf.exercise.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerService {

    Player create(long tournamentId, Player player);

    Player update(long id, Player player);

    List<Player> getPlayersByName(String name);

    List<Player> getPlayersByTournamentId(long id);

    void delete(long playerId);
}
