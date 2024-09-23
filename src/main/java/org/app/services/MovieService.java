package org.app.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.daos.GenreDAO;
import org.app.daos.MovieDAO;
import org.app.dtos.*;
import org.app.entities.Genre;
import org.app.entities.Movie;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieService /*implements IMovieService*/ {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final MovieDAO movieDAO;


    public MovieService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.movieDAO = new MovieDAO();
    }

    // Fetch movie details by ID
    public MovieDTO fetchMovieById(int movieId) throws Exception {
        String url = BASE_URL + "movie/" + movieId + "?api_key=" + API_KEY;
        return fetchMovieData(url);
    }

    // Fetch movie details by ID


    // Search movies by title
    public MovieDTO searchMovieByTitle(String title) throws Exception {
        String url = BASE_URL + "search/movie?api_key=" + API_KEY + "&query=" + title;
        return fetchMovieData(url);
    }

    // Search movies by release year
    public MovieListDTO searchMovieByReleaseYear(String year) throws Exception {
        String url = BASE_URL + "discover/movie?api_key=" + API_KEY + "&release_date.gte=" + year;
        return fetchMovieDataList(url);
    }

    // Search movies by rating
    public MovieDTO searchMovieByRating(String rating) throws Exception {
        String url = BASE_URL + "discover/movie?api_key=" + API_KEY + "&vote_average.gte=" + rating;
        return fetchMovieData(url);
    }

    public MovieListDTO getAllDanishMoviesFromYearTillNow(String year) throws Exception {
        return fetchMovieDataList(BASE_URL + "discover/movie?api_key=" + API_KEY +"&with_original_language=da&primary_release_date.gte="+year);
    }

    // Fetch movie data from API
    private MovieListDTO fetchMovieDataList(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("API Response: " + response.body()); // Print the raw response
            return objectMapper.readValue(response.body(), MovieListDTO.class);
        } else {
            throw new Exception("Failed to fetch movie data: " + response.statusCode());
        }
    }

    private MovieDTO fetchMovieData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), MovieDTO.class);
        } else {
            throw new Exception("Failed to fetch movie data: " + response.statusCode());
        }
    }

    /*public void saveAllDanishMoviesFromYearTillNow(String startDate) throws Exception {
        getAllDanishMoviesFromYearTillNow(startDate)
                .getResults()
                .forEach(movie -> {
                    movieDAO.create(
                            Movie.builder()
                                    .id((long) movie.getId())
                                    .title(movie.getTitle())
                                    .overview(movie.getOverview())
                                    .releaseDate(movie.getReleaseDate())
                                    .genres(movie.)
                                    .build()
                    );
                });
    }*/

    public void saveAllDanishMoviesFromYearTillNow(MovieListDTO movieList) throws Exception {
        GenreDAO genreDAO = new GenreDAO();

        for (MovieDTO movieDTO : movieList.getResults()) {
            Set<Genre> movieGenres = new HashSet<>();
            for (int genreId : movieDTO.getGenreIds()) {
                Genre genre = genreDAO.getBytId(genreId);
                if (genre != null) {
                    movieGenres.add(genre);
                }
            }

            Movie movie = Movie.builder()
                    .id((long) movieDTO.getId())
                    .title(movieDTO.getTitle())
                    .overview(movieDTO.getOverview())
                    .releaseDate(movieDTO.getReleaseDate())
                    .voteAverage(movieDTO.getVoteAverage())
                    .genres(movieGenres)
                    .build();

            movieDAO.create(movie);
        }
    }
/*
    public void getTopTenMovies(MovieListDTO movieList){
        List<MovieDTO> topTenList = movieList.getResults().stream()
                .sorted(Comparator.comparing(MovieDTO::getVoteAverage).reversed())
                .limit(10)
                .collect(Collectors.toList());
        for (MovieDTO movie : topTenList){
            System.out.println("Movie: "+movie.getTitle() + "\nRating: " + movie.getVoteAverage());
        }
    }

    public void getTopTenMostPopularMovies(MovieListDTO movieList){
        List<MovieDTO> topTenList = movieList.getResults().stream()
                .sorted(Comparator.comparing(MovieDTO::getPopularity).reversed())
                .limit(10)
                .collect(Collectors.toList());
        for (MovieDTO movie : topTenList){
            System.out.println("Movie: "+movie.getTitle() + "\nPopularity: " + movie.getPopularity());
        }
    }

    public void getLowestRatedTenMovies(MovieListDTO movieList) {
        List<MovieDTO> lowestTenList = movieList.getResults().stream()
                .sorted(Comparator.comparing(MovieDTO::getVoteAverage))
                .limit(10)
                .collect(Collectors.toList());

        for (MovieDTO movie : lowestTenList) {
            System.out.println("Movie: " + movie.getTitle() + "\nRating: " + movie.getVoteAverage());
        }
    }

    public double getAverageForAllMoviesInDB(MovieListDTO movieList) {
        return movieList.getResults().stream()
                .mapToDouble(MovieDTO::getVoteAverage)
                .average()
                .orElse(0.0);
    }*/

    public List<Movie> dbGetTopTenMovies(){
        return movieDAO.getTopTenMovies();
    }

    public List<Movie> dbGetTopTenMostPopularMovies(){
        return movieDAO.getTopTenMostPopularMovies();
    }

    public List<Movie> dbGetLowestRatedTenMovies() {
        return movieDAO.getLowestRatedTenMovies();
    }

    public double dbGetAverageForAllMoviesInDB() {
        return movieDAO.getAverageForAllMoviesInDB();
    }


    public Movie dbFetchMovieById(int movieId) throws Exception {
        return movieDAO.getBytId(movieId);
    }


    public List<Movie> dbSearchMovieByTitle(String title) throws Exception {
        return movieDAO.getByTitle(title);
    }


    public List<Movie> dbSearchMoviesByReleaseYear(String year) throws Exception {
        return movieDAO.getByReleaseYear(year);
    }


    public List<Movie> dbSearchMoviesByRating(double rating) throws Exception {
        return movieDAO.getByRating(rating);
    }




}
