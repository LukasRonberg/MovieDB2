package org.app;


import org.app.DAOS.MovieDAO;
import org.app.dtos.MovieListDTO;
import org.app.entities.Movie;
import org.app.services.MovieService;


public class Main {
    public static void main(String[] args) {
        try {
            MovieService movieService = new MovieService();
            String releaseYear = "2014";
            MovieListDTO movieListDTO = movieService.searchMovieByReleaseYear(releaseYear);

            System.out.println("Movies released in " + releaseYear + ":");
            MovieDAO movieDAO = new MovieDAO();

            movieListDTO.getResults().forEach(movie -> {
                movieDAO.create(Movie
                        .builder()
                        .title(movie.getTitle())
                        .overview(movie.getOverview())
                        .releaseDate(movie.getReleaseDate())
                        .build()
                );

                /*
                System.out.println("Movie Title: " + movie.getTitle());
                System.out.println("Overview: " + movie.getOverview());
                System.out.println("Release Date: " + movie.getReleaseDate());
                //System.out.println("Genres: " + movie.getGenres());
                System.out.println();*/
            });

            movieDAO.getAll().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

