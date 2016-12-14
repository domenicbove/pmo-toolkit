import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class GoogleDriveServiceImpl implements GoogleDriveService {
	
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Drive API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE);
    
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    /* Instantiate the drive service */
    Drive driveClient = getDriveService();
    
    
    public static Drive getDriveService() {
        Credential credential;
		try {
			credential = authorize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			credential = null;
			e.printStackTrace();
		}
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
	
	
	 public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            Quickstart.class.getResourceAsStream("/client_secret.json");//TODO this is dependent on Quickstart Class?
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
	public void initiateProjectFolder (String clientName, String projectName, List<String> emails) throws IOException {
		
		//Create the Folder with the Project Name and capture folder ID
		File folder = createFolder(clientName, projectName);
		String folderID = folder.getId();
		
        //Add the templates files to the folder
		addTemplateFiles(folderID, clientName, projectName);
        
        //Add the user permissions to the folder
        
	}
	
	private File createFolder(String clientName, String projectName) throws IOException {
		File fileMetadata = new File();
		String date = new SimpleDateFormat("MM-yyyy").format(new Date());
		fileMetadata.setName(date + " " + clientName + " - " + projectName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File folder = driveClient.files().create(fileMetadata).setFields("id").execute();
		return folder;
	}

	private void addTemplateFiles(String folderID, String clientName, String projectName) {
		 // File's content.	    
	    java.io.File filePath = new java.io.File("src/main/resources/EngagementJournal.docx");
		FileContent mediaContent = new FileContent("application/document",filePath);
		
		//Upload a file 
	    File fileToInsert = new File();
	    fileToInsert.setName(clientName +"-" + projectName + ":" + filePath.getName());
	    fileToInsert.setMimeType("application/file");

	    // Set the parent folder.
	    ArrayList<String> parentFolders = new ArrayList<String>();
	    if (folderID != null) {
	      parentFolders.add(folderID);
	      fileToInsert.setParents(parentFolders);
	      
	    }
	    
	    try {
			driveClient.files().create(fileToInsert, mediaContent).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void shareFile(File folder, List<String> emails) {
		// TODO Auto-generated method stub

	}

}
