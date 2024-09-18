package org.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.daos.GenreDAO;
import org.app.dtos.GenreListDTO;
import org.app.entities.Genre;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GenreService {
    private static final String API_KEY = "9ac5e8a8f5f3ade0552bb0f6dd5fd711";//System.getenv("API_KEY");  // Replace with your TMDB API key
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final GenreDAO genreDAO;

    public GenreService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.genreDAO = new GenreDAO();
    }

    public GenreListDTO fetchMovieGenres() throws Exception {
        String url = BASE_URL + "genre/movie/list?api_key=" + API_KEY;
        return fetchGenreData(url);
    }

    private GenreListDTO fetchGenreData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), GenreListDTO.class);
        } else {
            throw new Exception("Failed to fetch genre data: " + response.statusCode());
        }
    }

    public void saveMovieGenresToDB() throws Exception {
        fetchMovieGenres()
                .getGenres()
                .forEach(genre -> {
                    genreDAO.create(
                            Genre.builder()
                                    .id((long) genre.getId())
                                    .name(genre.getName())
                                    .build()
                    );
                });
    }
}
