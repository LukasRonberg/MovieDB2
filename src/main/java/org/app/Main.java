package org.app;


import org.app.daos.MovieDAO;
import org.app.dtos.GenreListDTO;
import org.app.dtos.MovieListDTO;
import org.app.entities.Genre;
import org.app.entities.Movie;
import org.app.services.CrewService;
import org.app.services.GenreService;
import org.app.services.MovieService;

import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) throws Exception {
        try {
            MovieService movieService = new MovieService();
            CrewService crewService = new CrewService();
            GenreService genreService = new GenreService();

            //metode til at oprette movie genre i databasen
            //genreService.saveMovieGenresToDB();

            // metode til at oprette danske film i databasen
            MovieListDTO movieList = movieService.getAllDanishMoviesFromYearTillNow("2024-01-01");
            //movieService.saveAllDanishMoviesFromYearTillNow(movieList);
            //movieService.getTopTenMovies(movieList);
            //movieService.getLowestRatedTenMovies(movieList);
            double vote = movieService.getAverageForAllMoviesInDB(movieList);
            System.out.println(vote);
            /*GenreListDTO genreListDTO = genreService.fetchMovieGenres();
            genreListDTO.getGenres().forEach(genre -> {
                System.out.println(genre.getId() + ": " + genre.getName());
            });*/

            //
            MovieDAO movieDAO = new MovieDAO();
            //List<Movie> movies = movieDAO.getAllWithGenres();

            // print alle film ud med deres genre
            /*for (Movie movie : movies) {
                System.out.println("Movie: " + movie.getTitle());
                System.out.println("Vote average: " + movie.getVoteAverage());
                System.out.println("Genres:");
                if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                    for (Genre genre : movie.getGenres()) {
                        System.out.println(" - " + genre.getName());
                    }
                } else {
                    System.out.println(" - No genres available");
                }
                System.out.println();
            }*/



            // metode til at finde alle crew medlemmer i databasen
            //TODO: Opret en ny DAO klasse til at håndtere crew medlemmer samt indsætte disse i database.
           /*MovieDAO movieDAO = new MovieDAO();
            for (Movie movie : movieDAO.getAll() ){
                System.out.println(movie.getTitle() + " - THIS IS THE TITLE");
                crewService.fetchCrewByMovieId(movie.getId().intValue()).getCast().forEach(System.out::println);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


