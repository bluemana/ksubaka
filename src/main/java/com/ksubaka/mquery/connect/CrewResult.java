package com.ksubaka.mquery.connect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CrewResult {

	private String name;
	private String job;
	
	public CrewResult() {
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
		return String.format("CrewResult[%s,%s]", name, job);
	}
}
