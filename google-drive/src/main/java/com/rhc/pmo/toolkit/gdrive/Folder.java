package com.rhc.pmo.toolkit.gdrive;

import java.util.List;

public class Folder {

	private String clientName;
	private String projectName;
	private List<String> emails;
	
	public Folder(String clientName, String projectName, List<String> emails) {
		this.clientName = clientName;
		this.projectName = projectName;
		this.emails = emails;
	}
	public Folder() {
		
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	@Override
	public String toString() {
		return "Folder [clientName=" + clientName + ", projectName=" + projectName + ", emails=" + emails + "]";
	}
	

	
}
