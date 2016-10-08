package com.ksubaka.mquery.connect.omdbapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResults {

	@JsonProperty("Response")
	private Boolean successful;
	@JsonProperty("Search")
	private List<SearchResult> results;
	@JsonProperty("totalResults")
	private Integer resultTotal;
	
	public Boolean getSuccessful() {
		return successful;
	}
	
	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}
	
	public List<SearchResult> getResults() {
		return results;
	}
	
	public void setResults(List<SearchResult> results) {
		this.results = results;
	}
	
	public Integer getResultTotal() {
		return resultTotal;
	}
	
	public void setResultTotal(Integer resultTotal) {
		this.resultTotal = resultTotal;
	}
}
