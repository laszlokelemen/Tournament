package com.paf.exercise.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Player.PLAYER)
public class Player {

    protected static final String PLAYER = "player";
    private static final String ID = " id";
    private static final String NAME = "name";

    @NotNull
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The name object is required!")
    @Column(name = NAME)
    private String name;

    @NotNull(message = "The tournament object is required!")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
}
