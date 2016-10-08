package com.ksubaka.mquery.connect.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ksubaka.mquery.connect.StringDeserializer;

public class MovieResponse {

	@JsonProperty("Director")
	@JsonDeserialize(using=StringDeserializer.class)
	private String director;
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
}
