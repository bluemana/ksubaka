package com.ksubaka.mquery.connect.tmdb;

public class CrewMember {

	private String name;
	private String job;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getJob() {
		return job;
	}
	
	public void setJob(String job) {
		this.job = job;
	}
	
	@Override
	public String toString() {
		return String.format("CrewMember[%s,%s]", name, job);
	}
}
