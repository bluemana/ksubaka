package com.ksubaka.mquery;

import java.util.Objects;

public class Movie {
	
	private final String title;
	private final Integer releaseYear;
	private final String director;
	
	public Movie(String title, Integer releaseYear, String director) {
		this.title = title;
		this.releaseYear = releaseYear;
		this.director = director;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Integer getReleaseYear() {
		return releaseYear;
	}
	
	public String getDirector() {
		return director;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Movie) {
			Movie movie = (Movie) obj;
			result = Objects.equals(title,movie.title) &&
				Objects.equals(releaseYear, movie.releaseYear) &&
				Objects.equals(director, movie.director);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(title, releaseYear, director);
	}
	
	@Override
	public String toString() {
		return String.format("Movie[%s,%s,%s]", title, releaseYear, director);
	}
}
