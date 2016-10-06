package ksubaka.mquery.connect;

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
import com.ksubaka.mquery.connect.Connector;


public class ConnectorTest {
	
	private static String API_KEY = "123456";
	
	@Test
	public void movies_NoMatch_EmptyList() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String jsonResponse = readResource("ConnectorTest_movies_NoMatch_EmptyList.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getSearchMovieUri(API_KEY, "", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));
		
		Connector connector = new Connector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("");
		Assert.assertTrue(movies.isEmpty());
	}
	
	@Test
	public void movies_Match_Retrieved() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String jsonResponse1 = readResource("ConnectorTest_movies_Match_Retrieved.json");
		String movie87CreditsJson = readResource("movie_87_credits.json");
		String movie89CreditsJson = readResource("movie_89_credits.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getSearchMovieUri(API_KEY, "indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(jsonResponse1, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getMovieCreditsUri(API_KEY, 87L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie87CreditsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getMovieCreditsUri(API_KEY, 89L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie89CreditsJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new Connector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("indiana jones");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Indiana Jones and the Temple of Doom", "1984-05-23", "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Last Crusade", "1989-05-24", "Steven Spielberg"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	@Test
	public void movies_MultiplePageMatch_Retrieved() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String page1Json = readResource("ConnectorTest_movies_MultiplePageMatch_Retrieved_Page1.json");
		String page2Json = readResource("ConnectorTest_movies_MultiplePageMatch_Retrieved_Page2.json");
		String movie85CreditsJson = readResource("movie_85_credits.json");
		String movie87CreditsJson = readResource("movie_87_credits.json");
		String movie89CreditsJson = readResource("movie_89_credits.json");
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getSearchMovieUri(API_KEY, "indiana jones", 1)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(page1Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getMovieCreditsUri(API_KEY, 89L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie89CreditsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getMovieCreditsUri(API_KEY, 87L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie87CreditsJson, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getSearchMovieUri(API_KEY, "indiana jones", 2)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(page2Json, MediaType.APPLICATION_JSON));
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(Connector.getMovieCreditsUri(API_KEY, 85L)))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
			.andRespond(MockRestResponseCreators.withSuccess(movie85CreditsJson, MediaType.APPLICATION_JSON));
		
		Connector connector = new Connector(restTemplate, API_KEY);
		List<Movie> movies = connector.getMovies("indiana jones");
		List<Movie> expected = new ArrayList<Movie>();
		expected.add(new Movie("Indiana Jones and the Last Crusade", "1989-05-24", "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Temple of Doom", "1984-05-23", "Steven Spielberg"));
		expected.add(new Movie("Raiders of the Lost Ark", "1981-06-12", "Steven Spielberg"));
		Assert.assertTrue(movies.equals(expected));
	}
	
	private static String readResource(String resourceName) throws IOException {
		try (InputStream input = ConnectorTest.class.getResourceAsStream(resourceName);
				Scanner scanner = new Scanner(input)) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
