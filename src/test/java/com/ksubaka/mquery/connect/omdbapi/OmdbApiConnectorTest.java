package com.ksubaka.mquery.connect.omdbapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.ksubaka.mquery.Movie;
import com.ksubaka.mquery.connect.omdbapi.OmdbApiConnector;

public class OmdbApiConnectorTest {

	@Test
	public void movies_NoMatch_EmptyList() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String responseJson = readResource("search_not_found.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("");
		Assert.assertTrue(movies.isEmpty());
	}
	
	@Test
	public void movies_Match_Retrieved() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results.json");
		String movie1Json = readResource("movie_tt0083658.json");
		String movie2Json = readResource("movie_tt0126817.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0083658")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie1Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0126817")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie2Json, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("blade runner");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Blade Runner", 1982, "Ridley Scott"));
		expected.add(new Movie("Blade Runner", 1997, "Joseph D. Kucan"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Test
	public void movies_IntervalYear_FirstEntryYear() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_tt0103586.json");
		String movie1Json = readResource("movie_tt0103586.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("indiana jones")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0103586")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie1Json, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("indiana jones");
		Assert.assertTrue(movies.get(0).getReleaseYear() == 1992);
	}
	
	@Test
	public void movies_DirectorNotAvailable_NullDirector() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_tt0103586.json");
		String movie1Json = readResource("movie_tt0103586.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("indiana jones")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0103586")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie1Json, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("indiana jones");
		Assert.assertNull(movies.get(0).getDirector());
	}
	
	private static String readResource(String resourceName) throws IOException {
		try (InputStream input = OmdbApiConnectorTest.class.getResourceAsStream(resourceName);
				Scanner scanner = new Scanner(input)) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
