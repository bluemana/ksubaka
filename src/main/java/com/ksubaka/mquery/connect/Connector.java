package com.ksubaka.mquery.connect;

import java.util.List;

import com.ksubaka.mquery.Movie;

public interface Connector {

	public List<Movie> getMovies(String searchText);
}
