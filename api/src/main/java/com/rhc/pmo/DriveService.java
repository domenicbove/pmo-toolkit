package com.rhc.pmo;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

public class DriveService {
  
  private Drive drive;

  public DriveService(String accessToken){
    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
    setDrive(new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
      .setApplicationName("PMO Toolkit").build());
  }

  public Drive getDrive() {
    return drive;
  }

  public void setDrive(Drive drive) {
    this.drive = drive;
  }
}
