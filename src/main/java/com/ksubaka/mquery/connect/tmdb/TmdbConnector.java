package com.ksubaka.mquery.connect.tmdb;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.ksubaka.mquery.Movie;

public class TmdbConnector {
	
	private static final Logger LOGGER = LogManager.getLogger(TmdbConnector.class);
	private static final String BASE_URI = "http://api.themoviedb.org/3/";
	
	private final RestTemplate restTemplate;
	private final String apiKey;
	
	public TmdbConnector(RestTemplate restTemplate, String apiKey) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
	}
	
	public List<Movie> getMovies(String searchText) {
		List<Movie> movies = new ArrayList<Movie>();
		int pageCount = 1;
		int pageTotal = 1;
		while (pageCount <= pageTotal) {
			SearchResults searchResults = restTemplate.getForObject(
				getSearchUri(apiKey, searchText, pageCount), SearchResults.class);
			pageTotal = searchResults.getPageTotal();
			int movieTotal = searchResults.getResultTotal();
			for (SearchResult searchResult : searchResults.getResults()) {
				LOGGER.info("Aggregating movie data of movie {} of {}...", movies.size() + 1, movieTotal);
				Movie movie = null;
				MovieCredits movieCredits = restTemplate.getForObject(getMovieCreditsUri(apiKey, searchResult.getId()), MovieCredits.class);
				for (CrewMember crewMember : movieCredits.getCrew()) {
					if (crewMember.getJob().equals("Director")) {
						movie = new Movie(searchResult.getTitle(),
								searchResult.getReleaseDate(), crewMember.getName());
						break;
					}
				}
				if (movie == null) {
					movie = new Movie(searchResult.getTitle(),
							searchResult.getReleaseDate(), null);
				}
				movies.add(movie);
			}
			pageCount++;
		};
		return movies;
	}
	
	public static URI getSearchUri(String apiKey, String searchText, int page) {
		try {
			String queryString = UriUtils.encodeQuery(
				"api_key=" + apiKey +
				"&query=\"" + searchText + "\"" +
				"&page=" + page,
				"utf-8");
			return URI.create(BASE_URI + "search/movie?" + queryString);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static URI getMovieCreditsUri(String apiKey, Long movieId) {
		return URI.create(BASE_URI + "movie/" + movieId + "/credits?api_key=" + apiKey);
	}
}
