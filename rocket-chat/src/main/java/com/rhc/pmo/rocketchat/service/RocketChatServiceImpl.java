package com.rhc.pmo.rocketchat.service;

import org.springframework.stereotype.Component;

import com.rhc.pmo.rocketchat.model.RCSTATUS;

@Component
public class RocketChatServiceImpl implements RocketChatService {
	
	@SuppressWarnings("unused")
	private RocketChatServiceImpl(){}
	
	private String userId;
	private String token;
	
	//use constructor, not setter dependancy injection for this class
	public RocketChatServiceImpl(String token, String userId){
		
		this.userId = userId;
		this.token = token;
	}

	public String getRCStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public String createChatRoom(String chatRoomName) {
		return null;// TODO Auto-generated method stub
		
	}

	public String createPrivateGroup(String groupName) {
		return null;// TODO Auto-generated method stub
		
	}

	public void addUsersToGroup(String[] userNames, String groupName) {
		// TODO Auto-generated method stub
		
	}

	public void addUsersToChannel(String[] userNames, String channelName) {
		// TODO Auto-generated method stub
		
	}

	public String getChannelId(String channelName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGroupId(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserId(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
