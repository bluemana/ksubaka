package com.ksubaka.mquery.connect.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieResponse {

	@JsonProperty("Director")
	private String director;
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
}
