package com.ksubaka.mquery;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.web.client.RestTemplate;

import com.ksubaka.mquery.connect.Connector;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getCanonicalName());
	
	public static void main(String[] args) throws Exception {
		Options options = getOptions();
		CommandLineParser cmdLineParser = new DefaultParser();
		try {
			CommandLine cmdLine = cmdLineParser.parse(options, args);
			if (cmdLine.hasOption("help")) {
				printUsage(options);
			} else {
				String searchString = getSearchString(cmdLine);
				LOGGER.info("Searching for \"" + searchString + "\"...");
				Properties config = new Properties();
				try (InputStream input = new FileInputStream("config.properties")) {
					config.load(input);
					String apiKey = config.getProperty("api_key");
					Connector connector = new Connector(new RestTemplate(), apiKey);
					List<Movie> movies = connector.getMovies(searchString);
					for (Movie movie : movies) {
						System.out.println(format(movie));
					}
				}
			}
		} catch (ParseException e) {
			printUsage(options);
			System.exit(1);
		}
	}
	
	private static Options getOptions() {
		Options options = new Options();
		options.addOption(Option.builder("?")
			.longOpt("help")
			.hasArg(false)
			.desc("print this help message")
			.required(false)
			.build());
		options.addOption(Option.builder("m")
			.longOpt("movie")
			.hasArgs()
			.desc("a space-separated list of keywords to search for movies")
			.required()
			.build());
		return options;
	}
	
	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("mquery", options, true);
	}
	
	private static String getSearchString(CommandLine cmdLine) {
		String[] keywords = cmdLine.getOptionValues("movie");
		String searchString = "";
		for (String keyword : keywords) {
			searchString += keyword + " ";
		}
		return searchString.trim();
	}
	
	private static String format(Movie movie) {
		return String.format("%s (%s) - %s",
			movie.getTitle(), movie.getReleaseDate(), movie.getDirector());
	}
}
