package com.rhc.pmo.toolkit.gdrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Create;

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

public class Quickstart {
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

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            Quickstart.class.getResourceAsStream("/client_secret.json");
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

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    /** Initialize a folder in Google Drive and add the necessary permissions.
     * @param The authorized drive client service.
     * @param The desired name of the folder to be created as a String.
     * @throws IOException
     */
    private static void initializeFolder(Drive service, String clientName, String projectName) throws IOException {
    	File fileMetadata = new File();
		String date = new SimpleDateFormat("MM-yyyy").format(new Date());
		fileMetadata.setName(date + "BankAm" + " - " + "BRMSProject");
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File folder = service.files().create(fileMetadata).setFields("id").execute();
	

	    // File's content.	    
	    java.io.File filePath = new java.io.File("src/main/resources/EngagementJournal.docx");
		FileContent mediaContent = new FileContent("application/document",filePath);
		
		//Upload a file 
	    File ejFile = new File();
	    ejFile.setName(clientName +"-" + projectName + ":" + filePath.getName());
	    ejFile.setMimeType("application/file");

	    // Set the parent folder.
	    ArrayList<String> parentFolders = new ArrayList<String>();
	    if (folder.getId() != null) {
	      parentFolders.add(folder.getId());
	      ejFile.setParents(parentFolders);
	    }
	    
	    service.files().create(ejFile, mediaContent).execute();
		
		
		//Add Permissions
      
       /* service.permissions().create(file.getId(),perm1).execute();*/
    }
    
    
    /**Create and return a Permissions object
     * @param String of the email to be added as a user
     */
    private static Permission createPermission(String email) {
    	
        Permission newPermission1 = new Permission();
	        newPermission1.setEmailAddress(email);
	        newPermission1.setType("user");
	        newPermission1.setRole("writer");
	       	
	   return newPermission1;
    }
    
    
    public static void main(String[] args) throws IOException {
        //Drive service = getDriveService();
        
        //initializeFolder(service,"SampleBank", "BRMSThing");

    }

}
