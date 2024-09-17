package org.app;


import org.app.daos.MovieDAO;
import org.app.dtos.MovieListDTO;
import org.app.entities.Movie;
import org.app.services.CrewService;
import org.app.services.MovieService;


public class Main {
    public static void main(String[] args) throws Exception {
        try {
            MovieService movieService = new MovieService();
            CrewService crewService = new CrewService();


            // metode til at oprette danske film i databasen
            /*MovieDAO movieDAO = new MovieDAO();

            movieService.getAllDanishMoviesFromYearTillNow("2024-01-01").getResults().forEach(movie -> {
                movieDAO.create(Movie
                        .builder()
                        .id((long) movie.getId())
                        .title(movie.getTitle())
                        .overview(movie.getOverview())
                        .releaseDate(movie.getReleaseDate())
                        .build()
                );
            });*/


            // metode til at finde alle crew medlemmer i databasen
            //TODO: Opret en ny DAO klasse til at håndtere crew medlemmer samt indsætte disse i database.
           MovieDAO movieDAO = new MovieDAO();
            for (Movie movie : movieDAO.getAll() ){
                System.out.println(movie.getTitle() + " - THIS IS THE TITLE");
                crewService.fetchCrewByMovieId(movie.getId().intValue()).getCast().forEach(System.out::println);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


