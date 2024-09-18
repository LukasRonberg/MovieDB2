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
@EqualsAndHashCode(exclude = "movies")

public class Genre {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    private Set<Movie> movies = new HashSet<>();
}
