package com.ksubaka.mquery.connect.tmdb;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchResults {

	private List<SearchResult> results;
	private int page;
	@JsonProperty("total_pages")
	private int pageTotal;
	@JsonProperty("total_results")
	private int resultTotal;
	
	public SearchResults() {
	}
	
	public List<SearchResult> getResults() {
		return results;
	}
	
	public void setResults(List<SearchResult> results) {
		this.results = results;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageTotal() {
		return pageTotal;
	}
	
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	
	public int getResultTotal() {
		return resultTotal;
	}
	
	public void setResultTotal(int resultTotal) {
		this.resultTotal = resultTotal;
	}
	
	@Override
	public String toString() {
		return String.format("SearchResults[%s,%s,%s,%s]",
			page, pageTotal, resultTotal, results.toString());
	}
}
