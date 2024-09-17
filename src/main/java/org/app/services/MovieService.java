package org.app.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.dtos.CrewDTO;
import org.app.dtos.CrewListDTO;
import org.app.dtos.MovieDTO;
import org.app.dtos.MovieListDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieService {
    private static final String API_KEY = "9ac5e8a8f5f3ade0552bb0f6dd5fd711";//System.getenv("API_KEY");  // Replace with your TMDB API key
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public MovieService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Fetch movie details by ID
    public MovieDTO fetchMovieById(int movieId) throws Exception {
        String url = BASE_URL + "movie/" + movieId + "?api_key=" + API_KEY;
        return fetchMovieData(url);
    }

    // Fetch movie details by ID
    public CrewListDTO fetchCrewByMovieId(int movieId) throws Exception {
        String url = BASE_URL + "movie/" + movieId + "/credits" +"?api_key=" + API_KEY;
        return fetchCrewData(url);
    }



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

    private CrewListDTO fetchCrewData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CrewListDTO.class);
        } else {
            throw new Exception("Failed to fetch crew data: " + response.statusCode());
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

    // Print movie overview
    public void printMovieOverview(int movieId) throws Exception {
        MovieDTO movie = fetchMovieById(movieId);
        System.out.println("Overview of the movie \"" + movie.getTitle() + "\": " + movie.getOverview());
    }
}
