package com.paf.exercise.controller;

import com.paf.exercise.model.Player;
import com.paf.exercise.service.api.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Validated
@RestController
@RequestMapping("/api")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Creates a new player.
     *
     * @param player - Player to create.
     * @return - Status 200 and the created player, 400 if the request body is faulty.
     */
    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody Player player) {
        Player createdPlayer = playerService.create(player.getTournament().getId(), player);
        return new ResponseEntity<>(createdPlayer, HttpStatus.OK);
    }

    /**
     * Returns a Player based on the given name parameter.
     *
     * @param name - The name of the player.
     * @return - Status 200 and list of the players, 404 if there is no player with the name param,
     * 400 if the request param is faulty.
     */
    @GetMapping(value = "/players", params = "name")
    public ResponseEntity<List<Player>> getPlayerByName(@NotBlank(message = "The name parameter must not be blank!")
                                                        @RequestParam("name") String name) {
        List<Player> player = playerService.getPlayersByName(name);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    /**
     * @return ArrayList of all visible widgets
     */


    /**
     * Updates a player object with the given ID.
     *
     * @param player - Player to update.
     * @return Status 200 and the updated players, 404 if there is no player with the ID,
     * 400 if the request param is faulty.
     */
    @PutMapping("/players")
    public ResponseEntity<Player> updatePlayer(@Valid @RequestBody Player player) {
        Player createdPlayer = playerService.update(player.getId(), player);
        return new ResponseEntity<>(createdPlayer, HttpStatus.OK);
    }

    /**
     * Returns the list of the players who are in the given tournament.
     *
     * @param id - The tournament ID.
     * @return Status 200 and list of the players, 404 if there is no tournament with the given ID,
     * 400 if the request param is faulty.
     */
    @GetMapping(value = "/players", params = "tournament_id")
    public ResponseEntity<List<Player>> getPlayersByTournamentId(@Min(0) @RequestParam("tournament_id") long id) {
        List<Player> player = playerService.getPlayersByTournamentId(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    /**
     * Deletes a player.
     *
     * @param id - The player ID.
     * @return Status 200 if the player has benn deleted,
     * 404 if there is no player with that id, 400 if the path variable is faulty.
     */
    @DeleteMapping(value = "/players/{id}")
    public ResponseEntity<HttpStatus> deletePlayer(@Min(0) @PathVariable long id) {
        playerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
