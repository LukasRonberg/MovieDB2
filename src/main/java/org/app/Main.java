package org.app;


import org.app.dtos.MovieListDTO;
import org.app.services.MovieService;



public class Main {
    public static void main(String[] args) {
        try {
            MovieService movieService = new MovieService();
            String releaseYear = "2014";
            MovieListDTO movieListDTO = movieService.searchMovieByReleaseYear(releaseYear);

            System.out.println("Movies released in " + releaseYear + ":");
            movieListDTO.getResults().forEach(movie -> {
                System.out.println("Movie Title: " + movie.getTitle());
                System.out.println("Overview: " + movie.getOverview());
                System.out.println("Release Date: " + movie.getReleaseDate());
                System.out.println();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

