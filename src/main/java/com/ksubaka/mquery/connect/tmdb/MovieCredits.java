package com.ksubaka.mquery.connect.tmdb;

import java.util.List;

public class MovieCredits {

	private List<CrewMember> crew;
	
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
