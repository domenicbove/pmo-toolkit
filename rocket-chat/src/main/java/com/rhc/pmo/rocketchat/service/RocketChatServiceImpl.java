package com.rhc.pmo.rocketchat.service;

import com.rhc.pmo.rocketchat.model.RCSTATUS;

public class RocketChatServiceImpl implements RocketChatService {

	public String getRCStatus() {
		return "RC is currently: " + RCSTATUS.UNKNOWN;
	}

	public void createChatRoom(String chatRoomName) {
		// TODO Auto-generated method stub
		
	}
	
	

}
