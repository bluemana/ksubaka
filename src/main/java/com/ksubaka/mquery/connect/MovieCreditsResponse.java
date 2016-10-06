package com.ksubaka.mquery.connect;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieCreditsResponse {

	private List<CrewResult> crew;
	
	public MovieCreditsResponse() {
	}
	
	public List<CrewResult> getCrew() {
		return crew;
	}
	
	public void setCrew(List<CrewResult> crew) {
		this.crew = crew;
	}
	
	@Override
	public String toString() {
		return crew.toString();
	}
}
