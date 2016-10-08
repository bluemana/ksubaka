package com.ksubaka.mquery.connect.tmdb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.ksubaka.mquery.Movie;
import com.ksubaka.mquery.connect.Connector;
import com.ksubaka.mquery.connect.ConnectorTest;


public class TmdbConnectorTest extends ConnectorTest {
	
	private static String API_KEY = "123456";
	
	@Override
	public void movies_NoMatch_EmptyList() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String responseJson = readResource("search_results_no_match.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getSearchUri(API_KEY, "", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new TmdbConnector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("");
		Assert.assertTrue(movies.isEmpty());
	}
	
	@Override
	public void movies_Match_List() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String searchResultsJson = readResource("search_results_page_1_of_1.json");
		String movie87CreditsJson = readResource("movie_87_credits.json");
		String movie89CreditsJson = readResource("movie_89_credits.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getSearchUri(API_KEY, "indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(searchResultsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 87L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie87CreditsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 89L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie89CreditsJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new TmdbConnector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("indiana jones");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Indiana Jones and the Temple of Doom", 1984, "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Last Crusade", 1989, "Steven Spielberg"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Override
	public void movies_MultiplePageMatch_List() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String page1Json = readResource("search_results_page_1_of_2.json");
		String page2Json = readResource("search_results_page_2_of_2.json");
		String movie85CreditsJson = readResource("movie_85_credits.json");
		String movie87CreditsJson = readResource("movie_87_credits.json");
		String movie89CreditsJson = readResource("movie_89_credits.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getSearchUri(API_KEY, "indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(page1Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 89L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie89CreditsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 87L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie87CreditsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getSearchUri(API_KEY, "indiana jones", 2)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(page2Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 85L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie85CreditsJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new TmdbConnector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("indiana jones");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Indiana Jones and the Last Crusade", 1989, "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Temple of Doom", 1984, "Steven Spielberg"));
		expected.add(new Movie("Raiders of the Lost Ark", 1981, "Steven Spielberg"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Override
	public void clean_NoReleaseYear_NullReleaseYear() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String resultJson = readResource("search_results_no_release_date.json");
		String movieCreditsJson = readResource("movie_368079_credits.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getSearchUri(API_KEY, "indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(resultJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 368079L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movieCreditsJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new TmdbConnector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("indiana jones");
		Assert.assertNull(movies.get(0).getReleaseYear());
	}
	
	@Override
	public void clean_NoDirector_NullDirector() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String resultJson = readResource("search_results_no_director.json");
		String movieCreditsJson = readResource("movie_368079_credits.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getSearchUri(API_KEY, "indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(resultJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TmdbConnector.getMovieCreditsUri(API_KEY, 368079L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movieCreditsJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new TmdbConnector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("indiana jones");
		Assert.assertNull(movies.get(0).getDirector());
	}
}
