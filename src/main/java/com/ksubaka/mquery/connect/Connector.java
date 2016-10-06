package com.ksubaka.mquery.connect;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.ksubaka.mquery.Movie;

public class Connector {
	
	private static final Logger LOGGER = Logger.getLogger(Connector.class.getCanonicalName());
	private static final String BASE_URI = "http://api.themoviedb.org/3/";
	
	private final RestTemplate restTemplate;
	private final String apiKey;
	
	public Connector(RestTemplate restTemplate, String apiKey) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
	}
	
	public List<Movie> getMovies(String searchText) {
		List<Movie> movies = new ArrayList<Movie>();
		SearchMoviesResponse searchMoviesResponse = restTemplate.getForObject(getSearchMovieUri(apiKey, searchText), SearchMoviesResponse.class);
		for (SearchMoviesResult searchMoviesResult : searchMoviesResponse.getResults()) {
			Movie movie = null;
			MovieCreditsResponse movieCreditResponse = restTemplate.getForObject(getMovieCreditsUri(apiKey, searchMoviesResult.getId()), MovieCreditsResponse.class);
			for (CrewResult crewResult : movieCreditResponse.getCrew()) {
				if (crewResult.getJob().equals("Director")) {
					movie = new Movie(searchMoviesResult.getTitle(),
							searchMoviesResult.getReleaseDate(), crewResult.getName());
					break;
				}
			}
			if (movie == null) {
				movie = new Movie(searchMoviesResult.getTitle(),
						searchMoviesResult.getReleaseDate(), null);
			}
			LOGGER.info(movie.toString());
			movies.add(movie);
		}
		return movies;
	}
	
	public static URI getSearchMovieUri(String apiKey, String searchText) {
		try {
			String queryString = UriUtils.encodeQuery("api_key=" + apiKey + "&query=\"" + searchText + "\"", "utf-8");
			return URI.create(BASE_URI + "search/movie?" + queryString);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static URI getMovieCreditsUri(String apiKey, Long movieId) {
		return URI.create(BASE_URI + "movie/" + movieId + "/credits?api_key=" + apiKey);
	}
}