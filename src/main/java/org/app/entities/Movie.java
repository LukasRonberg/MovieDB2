package org.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"genres", "crew"}) // Avoids infinite recursion
@EqualsAndHashCode(exclude = {"genres", "crew"}) // Avoids infinite recursion
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

    @Column(name = "popularity",nullable = false)
    private double popularity;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "MovieGenres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    /*@JoinTable(
            name = "MovieCrew",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "crew_id")
    )*/
    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private Set<Crew> crew;

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);  // Ensures the genre has this movie in its set
    }

}
