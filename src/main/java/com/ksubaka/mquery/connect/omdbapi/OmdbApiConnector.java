package com.ksubaka.mquery.connect.omdbapi;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.ksubaka.mquery.Movie;

public class OmdbApiConnector {

	private static final String BASE_URI = "http://www.omdbapi.com/";
	
	private final RestTemplate restTemplate;
	
	public OmdbApiConnector(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public List<Movie> getMovies(String searchText) {
		List<Movie> movies = new ArrayList<Movie>();
		SearchResults searchResponse = restTemplate.getForObject(getSearchUri(searchText), SearchResults.class);
		if (searchResponse.getSuccessful()) {
			for (SearchResult result : searchResponse.getResults()) {
				String imdbId = result.getImdbId();
				MovieResponse movieResponse = restTemplate.getForObject(getMovieUri(imdbId), MovieResponse.class);
				String director = movieResponse.getDirector();
				movies.add(new Movie(result.getTitle(), result.getYear(), director));
			}
		}
		return movies;
	}
	
	public static URI getSearchUri(String searchText) {
		try {
			String queryString = UriUtils.encodeQuery(
				"s=\"" + searchText + "\"",
				"utf-8");
			return URI.create(BASE_URI + "?" + queryString);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static URI getMovieUri(String movieImdbId) {
		return URI.create(BASE_URI + "?i=" + movieImdbId);
	}
}
