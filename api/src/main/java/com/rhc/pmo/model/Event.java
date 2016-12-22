package com.rhc.pmo.model;

import java.util.List;

public class Event {

	private String location;
	private String description;
	private List<String> emails;
	private String startDate;
	private String endDate;

	public Event() {
		
	}
	
	public Event(String location, String description, String startDate, String endDate, List<String> emails) {
		super();
		this.location = location;
		this.description = description;
		this.emails = emails;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String toString() {
		return "Event [location=" + location + ", description=" + description
				+ ", emails=" + emails + " ,start date =" + startDate + ", end date = " + endDate + "]";
	}
}
