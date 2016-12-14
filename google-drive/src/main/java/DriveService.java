import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class DriveService {
	
	  private Drive drive;

	  public DriveService(GoogleCredential credential){
	    setDrive(new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
	      .setApplicationName("PMO Toolkit").build());
	  }

	  public Drive getDrive() {
	    return drive;
	  }

	  public void setDrive(Drive drive) {
	    this.drive = drive;
	  }
	  
	  public File createFolder(String clientName, String projectName) throws IOException {
			File fileMetadata = new File();
			String date = new SimpleDateFormat("MM-yyyy").format(new Date());
			fileMetadata.setName(date + " " + clientName + " - " + projectName);
			fileMetadata.setMimeType("application/vnd.google-apps.folder");
			File folder = drive.files().create(fileMetadata).setFields("id").execute();
			return folder;
		}
	  
	 
}
