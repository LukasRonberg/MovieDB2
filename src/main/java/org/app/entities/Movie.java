package org.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "genres") // Avoids infinite recursion
@EqualsAndHashCode(exclude = "genres") // Avoids infinite recursion
public class Movie {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "overview",nullable = false, length = 1000)
    private String overview;

    @Column(name = "release_date",nullable = false)
    private String releaseDate;

    @Column(name = "vote_average",nullable = false)
    private double voteAverage;

    @ManyToMany
    @JoinTable(
            name = "MovieGenres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
}
