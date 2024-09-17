package org.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class MovieDTO {
    private int id;
    private String title;

    @JsonProperty("release_date")
    private String releaseDate;
    private String overview;

    @JsonProperty("vote_average")
    private double voteAverage;
    /*private double popularity;
    private String posterPath;*/
}

