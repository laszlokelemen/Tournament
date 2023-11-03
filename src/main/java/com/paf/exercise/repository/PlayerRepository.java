package com.paf.exercise.repository;

import com.paf.exercise.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByName(String name);

    List<Player> findByTournamentId(long id);
}
