package org.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.dtos.CrewListDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CrewService {
    private static final String API_KEY = System.getenv("API_KEY");  // Replace with your TMDB API key
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public CrewService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public CrewListDTO fetchCrewByMovieId(int movieId) throws Exception {
        String url = BASE_URL + "movie/" + movieId + "/credits" +"?api_key=" + API_KEY;
        return fetchCrewData(url);
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
}
