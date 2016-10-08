package com.ksubaka.mquery;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.ksubaka.mquery.connect.Api;
import com.ksubaka.mquery.connect.Connector;
import com.ksubaka.mquery.connect.ConnectorFactory;

public class Main {
	
	private static final Logger LOGGER = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) {
		Options options = getOptions();
		CommandLineParser cmdLineParser = new DefaultParser();
		try {
			CommandLine cmdLine = cmdLineParser.parse(options, args);
			if (cmdLine.hasOption("help")) {
				printUsage(options);
			} else {
				Api api = getApi(cmdLine);
				if (api != null) {
					try {
						Connector connector = ConnectorFactory.create(api, new RestTemplate());
						String searchString = getSearchString(cmdLine);
						LOGGER.info("Searching for \"{}\" from {}...", searchString, api.getShortName());
						List<Movie> movies = connector.getMovies(searchString);
						for (Movie movie : movies) {
							System.out.println(format(movie));
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						System.exit(1);
					}
				} else {
					printUsage(options);
					System.exit(1);
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
		options.addOption(Option.builder("a")
			.longOpt("api")
			.hasArg()
			.desc("use the specified API. Available options:\n" +
					"tmdb : the API of The Movie Database (themoviedb.org)\n" +
					"omdb : the API of Open Movie Database API (omdbapi.com)")
			.required(true)
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
	
	private static Api getApi(CommandLine cmdLine) {
		String apiString = cmdLine.getOptionValue("api");
		return Api.fromIdName(apiString);
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
			movie.getTitle(),
			movie.getReleaseYear() != null ? movie.getReleaseYear().toString() : "N/A",
			movie.getDirector() != null ? movie.getDirector() : "N/A");
	}
}
