package org.app;


import org.app.daos.MovieDAO;
import org.app.dtos.MovieListDTO;
import org.app.entities.Crew;
import org.app.entities.Movie;
import org.app.services.*;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            MovieService movieService = new MovieService();
            CrewService crewService = new CrewService();
            GenreService genreService = new GenreService();


            //setup
            //todo: kun såfremt at databasen er tom
            //setup(movieService, genreService, crewService);


            while (true) {

                System.out.println("Hello World!");
                System.out.println("This is a movie database project");
                System.out.println("Please select one of the options below:");

                System.out.println("1. Search movie by title");
                System.out.println("2. Fetch top 10 movies");
                System.out.println("3. Fetch lowest rated 10 movies");
                System.out.println("4. Fetch top 10 most popular movies");
                System.out.println("5. Fetch average rating for all movies in database");
                System.out.println("6. Fetch by release year");
                System.out.println("7. Fetch by rating");
                System.out.println("0. Exit");
                System.out.println("Please select an option:");
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("Enter movie title:");
                        String title = scanner.next();
                        movieService.dbSearchMovieByTitle(title).forEach(System.out::println);
                        break;
                    case 2:
                        movieService.dbGetTopTenMovies().forEach(System.out::println);
                        ;
                        break;
                    case 3:
                        movieService.dbGetLowestRatedTenMovies().forEach(System.out::println);
                        ;
                        break;
                    case 4:
                        movieService.dbGetTopTenMostPopularMovies().forEach(System.out::println);
                        ;
                        break;
                    case 5:
                        System.out.println(movieService.dbGetAverageForAllMoviesInDB());
                        break;
                    case 6:
                        System.out.println("Enter release year:");
                        String year = scanner.next();
                        movieService.dbSearchMoviesByReleaseYear(year).forEach(System.out::println);
                        ;
                        break;
                    case 7:
                        System.out.println("Enter min rating:");
                        double rating = scanner.nextDouble();
                        movieService.dbSearchMoviesByRating(rating).forEach(System.out::println);
                        ;
                        break;
                    case 0: // exit
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void setup(MovieService movieService, GenreService genreService, CrewService crewService) throws Exception {
        MovieListDTO movieList = movieService.getAllDanishMoviesFromYearTillNow("2024-01-01");
        movieService.saveAllDanishMoviesFromYearTillNow(movieList);
        //metode til at oprette movie genre i databasen
        genreService.saveMovieGenresToDB();

        org.app.daos.CrewDAO crewDAO = new org.app.daos.CrewDAO();
        MovieDAO movieDAO = new MovieDAO();
        for (Movie movie : movieDAO.getAll()) {
            System.out.println(movie.getTitle() + " - THIS IS THE TITLE");
            crewService.fetchCrewByMovieId(movie.getId().intValue()).getCast().forEach(crew -> {
                crewDAO.create(Crew.builder()
                        .name(crew.getName())
                        .department(crew.getDepartment())
                        .character(crew.getCharacter())
                        .movie(movie)
                        .build());
            });
        }


    }
}


