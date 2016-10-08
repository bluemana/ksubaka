package com.ksubaka.mquery.connect.omdbapi;

import java.util.ArrayList;
import java.util.List;

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
import com.ksubaka.mquery.connect.ConnectorTest;

public class OmdbApiConnectorTest extends ConnectorTest {

	@Override
	public void movies_NoMatch_EmptyList() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String responseJson = readResource("search_not_found.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("");
		Assert.assertTrue(movies.isEmpty());
	}
	
	@Override
	public void movies_Match_List() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results.json");
		String movie1Json = readResource("movie_tt0083658.json");
		String movie2Json = readResource("movie_tt1080585.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0083658")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie1Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt1080585")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie2Json, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("blade runner");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Blade Runner", 1982, "Ridley Scott"));
		expected.add(new Movie("Dangerous Days: Making Blade Runner", 2007, "Charles de Lauzirika"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Override
	public void movies_MultiplePageMatch_List() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String page1Json = readResource("search_results_page_1_of_2.json");
		String page2Json = readResource("search_results_page_2_of_2.json");
		String movie1Json = readResource("movie_tt0083658.json");
		String movie2Json = readResource("movie_tt1080585.json");
		String movie3Json = readResource("movie_tt1846491.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(page1Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0083658")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie1Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt1080585")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie2Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner", 2)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(page2Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt1846491")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie3Json, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("blade runner");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Blade Runner", 1982, "Ridley Scott"));
		expected.add(new Movie("Dangerous Days: Making Blade Runner", 2007, "Charles de Lauzirika"));
		expected.add(new Movie("Blade Runner 60: Director's Cut", 2012, "Richard Cosgrove"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Test
	public void movies_MovieType_List() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_movie_type.json");
		String movieJson = readResource("movie_tt0083658.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0083658")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movieJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("blade runner");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Blade Runner", 1982, "Ridley Scott"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Test
	public void movies_GameType_NotInList() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_game_type.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("blade runner");
		Assert.assertTrue(movies.isEmpty());
	}
	
	@Override
	public void clean_NoReleaseYear_NullReleaseYear() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_no_year.json");
		String movieJson = readResource("movie_tt0281011.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("blade runner", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0281011")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movieJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("blade runner");
		Assert.assertNull(movies.get(0).getReleaseYear());
	}
	
	@Override
	public void clean_NoDirector_NullDirector() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_tt0103586.json");
		String movieJson = readResource("movie_tt0103586.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0103586")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movieJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("indiana jones");
		Assert.assertNull(movies.get(0).getDirector());
	}
	
	@Test
	public void clean_IntervalReleaseYear_FirstEntryYear() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_tt0103586.json");
		String movieJson = readResource("movie_tt0103586.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getSearchUri("indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(OmdbApiConnector.getMovieUri("tt0103586")))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movieJson, MediaType.APPLICATION_JSON));
		
		OmdbApiConnector connector = new OmdbApiConnector(restTemplate);
		List<Movie> movies = connector.getMovies("indiana jones");
		Assert.assertTrue(movies.get(0).getReleaseYear() == 1992);
	}
}
