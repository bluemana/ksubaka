package com.ksubaka.mquery.connect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CrewMember {

	private String name;
	private String job;
	
	public CrewMember() {
	}
	
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
