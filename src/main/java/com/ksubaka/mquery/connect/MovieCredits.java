package com.ksubaka.mquery.connect;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieCredits {

	private List<CrewMember> crew;
	
	public MovieCredits() {
	}
	
	public List<CrewMember> getCrew() {
		return crew;
	}
	
	public void setCrew(List<CrewMember> crew) {
		this.crew = crew;
	}
	
	@Override
	public String toString() {
		return String.format("MovieCredits[%s]", crew.toString());
	}
}
