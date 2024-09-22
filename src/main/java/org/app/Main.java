package org.app;


import org.app.daos.CrewDAO;
import org.app.daos.MovieDAO;
import org.app.dtos.CrewDTO;
import org.app.dtos.MovieListDTO;
import org.app.entities.Crew;
import org.app.entities.Movie;
import org.app.services.*;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Main {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            MovieService movieService = new MovieService();
            MovieDAO movieDAO = new MovieDAO();
            CrewDAO crewDAO = new CrewDAO();
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
                        //movieService.dbSearchMovieByTitle(title).forEach(System.out::println);
                        List<Movie> movies = movieService.dbSearchMovieByTitle(title);
                        if (movies.isEmpty()) {
                            System.out.println("No movies found with the title.");
                        } else {
                            for (int i = 0; i < movies.size(); i++) {
                                System.out.println((i + 1) + ". " + movies.get(i));
                            }

                            System.out.println("Select a movie by number:");
                            int choice = scanner.nextInt();

                            if (choice >= 1 && choice <= movies.size()) {
                                Movie selectedMovie = movies.get(choice - 1);
                                showMovieOptions(selectedMovie, scanner, movieDAO,crewDAO);
                            } else {
                                System.out.println("Invalid selection.");
                            }
                        }
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

    private static void showMovieOptions(Movie selectedMovie, Scanner scanner, MovieDAO movieDAO, CrewDAO crewDAO) {
        System.out.println("\n1. Update movie \n2. Delete movie \n3. Show directors \n4. Show actors \n5. Cancel");

        int option = scanner.nextInt();
        switch (option) {
            case 1:
                updateMovie(selectedMovie, scanner, movieDAO);
                break;
            case 2:
                deleteMovie(selectedMovie, scanner, movieDAO);
                break;
            case 3:
                showDirectors(selectedMovie, crewDAO);
                break;
            case 4:
                showActors(selectedMovie, crewDAO);
                break;
            case 5:
                System.out.println("Returning...");
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    private static void updateMovie(Movie selectedMovie, Scanner scanner,MovieDAO movieDAO) {
        System.out.println("Enter new title (current: " + selectedMovie.getTitle() + "): ");
        String newTitle = scanner.next();
        selectedMovie.setTitle(newTitle);

        System.out.println("Enter new release date (current: " + selectedMovie.getReleaseDate() + "): ");
        String newReleaseDate = scanner.next();
        selectedMovie.setReleaseDate(newReleaseDate);

        movieDAO.update(selectedMovie);
        System.out.println("Movie updated successfully.");
    }

    private static void deleteMovie(Movie selectedMovie, Scanner scanner, MovieDAO movieDAO) {
        System.out.println("Are you sure you want to delete the movie? (y/n)");
        String confirmation = scanner.next();

        if (confirmation.equalsIgnoreCase("y")) {
            movieDAO.delete(selectedMovie);
            System.out.println("Movie deleted successfully.");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private static void showDirectors(Movie selectedMovie, CrewDAO crewDAO) {
        Set<Crew> directors = crewDAO.getActorsOrDirectors(selectedMovie, true);
        if (directors.isEmpty()) {
            System.out.println("No directors found for this movie.");
        } else {
            System.out.println("Directors:");
            directors.forEach(director -> System.out.println(director.getName()));
        }
    }

    private static void showActors(Movie selectedMovie, CrewDAO crewDAO) {
        Set<Crew> actors = crewDAO.getActorsOrDirectors(selectedMovie, false);
        if (actors.isEmpty()) {
            System.out.println("No actors found for this movie.");
        } else {
            System.out.println("Actors:");
            actors.forEach(actor -> System.out.println(actor.getName()));
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
                        .job(crew.getJob())
                        .movie(movie)
                        .build());
            });

            crewService.fetchCrewByMovieId(movie.getId().intValue()).getCrew().forEach(crew -> {
                if (crew.getJob().equals("Director")) {
                    // Found the director
                    // Access the director's job
                    String directorJob = crew.getJob();
                    // Create a Crew object and set the job field
                    Crew crewEntity = Crew.builder()
                            .name(crew.getName())
                            .department(crew.getDepartment()) // Use department for general role
                            .character(crew.getCharacter())
                            .job(directorJob)
                            .movie(movie)
                            .build();

                    // Persist the Crew object to the database
                    crewDAO.create(crewEntity);
                }
            });
        }


    }
}


