package com.ksubaka.mquery;

import java.util.Objects;

public class Movie {
	
	private final String title;
	private final String releaseDate;
	private final String director;
	
	public Movie(String title, String releaseDate, String director) {
		this.title = title;
		this.releaseDate = releaseDate;
		this.director = director;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public String getDirector() {
		return director;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Movie) {
			Movie movie = (Movie) obj;
			result = title.equals(movie.title) &&
				releaseDate.equals(movie.releaseDate) &&
				director.equals(movie.director);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(title, releaseDate, director);
	}
	
	@Override
	public String toString() {
		return String.format("Movie[%s,%s,%s]", title, releaseDate, director);
	}
}
