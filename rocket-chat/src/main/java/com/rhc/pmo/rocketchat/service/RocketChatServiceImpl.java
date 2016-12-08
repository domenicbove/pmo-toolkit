package com.rhc.pmo.rocketchat.service;

import org.springframework.stereotype.Component;

import com.rhc.pmo.rocketchat.model.RCSTATUS;

@Component
public class RocketChatServiceImpl implements RocketChatService {

	public String getRCStatus() {
		return "RC is currently: " + RCSTATUS.UNKNOWN;
	}

	public void createChatRoom(String chatRoomName) {
		// TODO Auto-generated method stub
		
	}
	
	

}
