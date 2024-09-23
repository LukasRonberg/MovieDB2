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
@ToString(exclude = "movie")  // Exclude movie to avoid recursion
@EqualsAndHashCode(exclude = "movie")  // Exclude movie to avoid recursion
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "character")
    private String character;

    @Column(name = "department")
    private String department;

    @Column(name = "job")
    private String job;


    @ManyToOne(/*fetch = FetchType.EAGER*/)
    private Movie movie;
}
