package org.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "character",nullable = false)
    private String character;

    @Column(name = "department",nullable = false)
    private String department;

    @ManyToOne(/*fetch = FetchType.EAGER*/)
    private Movie movie;
}
