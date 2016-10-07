package com.ksubaka.mquery.connect;

public enum Api {

	TMDB("TMDb", "tmdb"),
	OMDB_API("OMDb API", "omdb");
	
	private final String shortName;
	private final String idName;
	
	private Api(String shortName, String idName) {
		this.shortName = shortName;
		this.idName = idName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public String getIdName() {
		return idName;
	}
	
	@Override
	public String toString() {
		return shortName;
	}
	
	public static Api fromIdName(String idName) {
		Api api = null;
		if (idName.equals("tmdb")) {
			api = TMDB;
		} else if (idName.equals("omdb")) {
			api = OMDB_API;
		}
		return api;
	}
}
