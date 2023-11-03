package com.paf.exercise.service.api;

import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TournamentService {

    Tournament create(Tournament tournament);

    Tournament update(long id, Tournament tournament);

    void delete(long id);

    List<Tournament> getTournaments();

    Tournament getTournamentById(long id);

    List<Player> getPlayers(long id);
}
