package com.paf.exercise.service.impl;

import com.paf.exercise.exception.InvalidInputException;
import com.paf.exercise.exception.ResourceNotFoundException;
import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.repository.TournamentRepository;
import com.paf.exercise.service.api.TournamentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class TournamentServiceImpl implements TournamentService {
    protected static final String TOURNAMENT_NOT_FOUND_ERROR = "The tournament with id: %s does not exist!";

    private final TournamentRepository tournamentRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }


    @Transactional
    @Override
    public Tournament create(Tournament tournament) {
        long id = tournament.getId();
        if (tournamentRepository.existsById(id)) {
            throw new InvalidInputException(String.format("The tournament is already created with id: %s!", id));
        }
        return tournamentRepository.save(tournament);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    @Transactional
    @Override
    public Tournament update(long id, Tournament tournament) {
        Tournament updatedTournament = tournamentRepository.findById(id)
                .map(updateTournament -> {
                    updateTournament.setRewardAmount(tournament.getRewardAmount());
                    updateTournament.setPlayers(tournament.getPlayers());
                    return tournamentRepository.save(tournament);
                }).orElseThrow(() -> new ResourceNotFoundException(format(TOURNAMENT_NOT_FOUND_ERROR, id)));

        log.info(format("The Tournament %s has been updated successfully!", id));
        return updatedTournament;
    }

    @Transactional(readOnly = true)
    @Override
    public Tournament getTournamentById(long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(format(TOURNAMENT_NOT_FOUND_ERROR, id)));
    }

    @Transactional
    @Override
    public void delete(long id) {
       tournamentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(format(TOURNAMENT_NOT_FOUND_ERROR, id)));
       tournamentRepository.deleteById(id);
       log.info(format("The tournament with id:%s is deleted from the tournament!", id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Player> getPlayers(long id) {
        Tournament tournament = getTournamentById(id);
        return tournament.getPlayers();
    }
}
