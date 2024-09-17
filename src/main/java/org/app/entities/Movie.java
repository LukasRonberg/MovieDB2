package org.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movie {

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /*@Column(name = "original_movie_id",nullable = false)
    private Long originalMovieId;*/

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "overview",nullable = false, length = 1000)
    private String overview;

    @Column(name = "release_date",nullable = false)
    private String releaseDate;
}
