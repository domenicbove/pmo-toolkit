package com.rhc.pmo.model;

import java.util.List;

public class Event {

	private String location;
	private String description;
	private List<String> emails;
	
	public Event() {
		
	}
	
	public Event(String location, String description, List<String> emails) {
		super();
		this.location = location;
		this.description = description;
		this.emails = emails;
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
	
	@Override
	public String toString() {
		return "Event [location=" + location + ", description=" + description
				+ ", emails=" + emails + "]";
	}
}
