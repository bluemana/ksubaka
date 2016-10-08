package com.ksubaka.mquery.connect.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ksubaka.mquery.connect.StringDeserializer;

public class SearchResult {

	@JsonProperty("Title")
	private String title;
	@JsonProperty("Year")
	@JsonDeserialize(using=StringDeserializer.class)
	private String year;
	@JsonProperty("imdbID")
	private String imdbId;
	@JsonProperty("Type")
	private String type;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getImdbId() {
		return imdbId;
	}
	
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
