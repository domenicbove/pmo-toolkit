package hello;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

public class DriveService {
  
  private Drive drive;

  public DriveService(String accessToken){
    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
    drive = new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
      .setApplicationName("first try").build();
    
    System.out.println(drive.getBaseUrl());
    
    
//    Plus plus = new Plus.builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
//        .setApplicationName("Google-PlusSample/1.0")
//        .build();
  }
}
