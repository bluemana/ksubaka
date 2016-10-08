package com.ksubaka.mquery.connect.omdbapi;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.ksubaka.mquery.Movie;
import com.ksubaka.mquery.connect.Connector;

public class OmdbApiConnector implements Connector {

	private static final Logger LOGGER = LogManager.getLogger(OmdbApiConnector.class);
	private static final String BASE_URI = "http://www.omdbapi.com/";
	
	private final RestTemplate restTemplate;
	
	public OmdbApiConnector(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public List<Movie> getMovies(String searchText) {
		List<Movie> movies = new ArrayList<Movie>();
		SearchResults searchResults = restTemplate.getForObject(getSearchUri(searchText), SearchResults.class);
		if (searchResults.getSuccessful()) {
			for (SearchResult searchResult : searchResults.getResults()) {
				LOGGER.info("Aggregating movie data of movie {} of {}...", movies.size() + 1, searchResults.getResults().size());
				String imdbId = searchResult.getImdbId();
				MovieResponse movieResponse = restTemplate.getForObject(getMovieUri(imdbId), MovieResponse.class);
				String director = movieResponse.getDirector();
				movies.add(new Movie(searchResult.getTitle(), parseYear(searchResult.getYear()), director));
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
	
	private static Integer parseYear(String year) {
		Integer result = null;
		if (year != null) {
			result = Integer.parseInt(year.split("[-–]")[0]);
		}
		return result;
	}
}
