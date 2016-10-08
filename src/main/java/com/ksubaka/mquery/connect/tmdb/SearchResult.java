package com.ksubaka.mquery.connect.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ksubaka.mquery.connect.StringDeserializer;

public class SearchResult {

	private Long id;
	@JsonProperty("release_date")
	@JsonDeserialize(using=StringDeserializer.class)
	private String releaseDate;
	private String title;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return String.format("SearchResult[%d,%s,%s]", id, releaseDate, title);
	}
}
