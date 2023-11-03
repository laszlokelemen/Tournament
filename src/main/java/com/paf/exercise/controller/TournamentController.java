package com.paf.exercise.controller;

import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.service.api.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class TournamentController {
    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    /**
     * Creates a new tournament
     *
     * @param tournament - Tournament to create.
     * @return Status 200 and the created tournament, 400 if the request param is faulty.
     */
    @PostMapping("/tournaments")
    public ResponseEntity<Tournament> createTournament(@Valid @RequestBody Tournament tournament) {
        Tournament createdTournament = tournamentService.create(tournament);
        return new ResponseEntity<>(createdTournament, HttpStatus.OK);
    }

    /**
     * Deletes a tournament.
     *
     * @param id - Tournament ID.
     * @return Status 200 and if the tournament has been deleted, 400 if the request param is faulty,
     * 404 if the tournament with the ID param is missing.
     */
    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<HttpStatus> deleteTournament(@Min(0) @PathVariable("id") long id) {
        tournamentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns a tournament with the given ID.
     *
     * @param id - Tournament ID.
     * @return Status Code 200 and the tournament, 400 if the request param is faulty,
     * 404 if the tournament with the ID param is missing.
     */
    @GetMapping("/tournaments/{id}")
    public ResponseEntity<Tournament> getTournament(@Min(0) @PathVariable("id") long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    /**
     * Return all the tournaments.
     *
     * @return Status Code 200 and the list of the tournaments, 400 if the request param is faulty.
     */
    @GetMapping("/tournaments")
    public ResponseEntity<List<Tournament>> getTournaments() {
        List<Tournament> tournament = tournamentService.getTournaments();
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    /**
     * Return the list of the players from a given tournament.
     *
     * @param id - Tournament ID.
     * @return Status Code 200 and the list of the players, 400 if the request param is faulty,
     * 404 there is no tournament with the ID.
     */
    @GetMapping("/tournaments/{id}/players")
    public ResponseEntity<List<Player>> getPlayers(@Min(0) @PathVariable("id") long id) {
        List<Player> players = tournamentService.getPlayers(id);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    /**
     * Updates a tournament.
     *
     * @param tournamentId      - Tournament ID.
     * @param tournamentUpdated - Tournament to update.
     * @return Status Code 200 and the updated tournament, 400 if the request param is faulty,
     * 404 there is no tournament with the ID.
     */
    @PutMapping("/tournaments/{id}")
    public ResponseEntity<Tournament> update(@Positive @PathVariable("id") long tournamentId, @Valid @RequestBody Tournament tournamentUpdated) {
        tournamentService.update(tournamentId, tournamentUpdated);
        return new ResponseEntity<>(tournamentUpdated, HttpStatus.OK);
    }
}
