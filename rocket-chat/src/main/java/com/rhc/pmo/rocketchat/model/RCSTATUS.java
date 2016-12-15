package com.rhc.pmo.rocketchat.model;

public enum RCSTATUS {
	
	UP("Rocket.Chat is up and running"),
	DOWN("Rocket.Chat is not responding"),
	UNKNOWN("Rocket.Chat status is unknown, please check logs");
	
	private RCSTATUS(String description) {
		this.statusDesc = description;
	}

	private String statusDesc;
	
	public String getStatusDescription(){
		return statusDesc;
	}

}
