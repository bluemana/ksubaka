package com.ksubaka.mquery.connect;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchMoviesResponse {

	private List<SearchMoviesResult> results;
	
	public SearchMoviesResponse() {
	}
	
	public List<SearchMoviesResult> getResults() {
		return results;
	}
	
	public void setResults(List<SearchMoviesResult> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return results.toString();
	}
}
