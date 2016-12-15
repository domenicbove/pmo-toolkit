package com.rhc.pmo.rocketchat.service;

public interface RocketChatService {

	public String getRCStatus();
	public void createChatRoom(String chatRoomName);
	public void createPrivateGroup(String groupName);
	public void addUsersToGroup(String[] userNames, String groupName);
	public void addUsersToChannel(String[] userNames, String channelName);
	
}
