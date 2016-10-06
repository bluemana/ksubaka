package com.ksubaka.mquery;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.web.client.RestTemplate;

import com.ksubaka.mquery.connect.Connector;

public class Main {

	public static void main(String[] args) throws Exception {
		Properties config = new Properties();
		try (InputStream input = new FileInputStream("config.properties")) {
			config.load(input);
			String apiKey = config.getProperty("api_key");
			Connector connector = new Connector(new RestTemplate(), apiKey);
			List<Movie> movies = connector.getMovies("indiana jones");
			for (Movie movie : movies) {
				System.out.println(format(movie));
			}
		}
	}
	
	private static String format(Movie movie) {
		return String.format("%s (%s) - %s",
			movie.getTitle(), movie.getReleaseDate(), movie.getDirector());
	}
}
