package com.paf.exercise.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tournament.TOURNAMENT)
public class Tournament {

    protected static final String TOURNAMENT = "tournament";
    private static final String ID = " id";
    private static final String REWARD_AMOUNT = "reward_amount";

    @NotNull
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = TOURNAMENT)
    private List<Player> players;


    @NotNull(groups = Tournament.class)
    @Null(groups = Player.class)
    @Positive(message = "The reward object amount must be greater than 0!")
    @Column(name = REWARD_AMOUNT)
    private int rewardAmount;
}
