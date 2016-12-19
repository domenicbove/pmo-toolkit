package com.rhc.pmo.toolkit.gdrive;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

public class DriveService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DriveService.class);
	private Drive drive;

	public DriveService(GoogleCredential credential) {
		setDrive(new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
				.setApplicationName("PMO Toolkit").build());
	}

	public Drive getDrive() {
		return drive;
	}

	public void setDrive(Drive drive) {
		this.drive = drive;
	}

	public void initiateProjectFolder(String clientName, String projectName, List<String> emails) throws IOException {

		// Create the Folder with the Project Name and capture folder ID
		File folder = createFolder(clientName, projectName);
		String folderID = folder.getId();

		// Add the templates files to the folder
		//addTemplateFiles(folderID, clientName, projectName);

		// Add the user permissions to the folder

	}

	public File createFolder(String clientName, String projectName) throws IOException {
		File fileMetadata = new File();
		String date = new SimpleDateFormat("MM-yyyy").format(new Date());
		fileMetadata.setName(date + " " + clientName + " - " + projectName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File folder = drive.files().create(fileMetadata).setFields("id").execute();
		LOGGER.info("here");
		return folder;
	}

	private void addTemplateFiles(String folderID, String clientName, String projectName) {
		// File's content.
		java.io.File filePath = new java.io.File("src/main/resources/EngagementJournal.docx");
		FileContent mediaContent = new FileContent("application/document", filePath);

		// Upload a file
		File fileToInsert = new File();
		fileToInsert.setName(clientName + "-" + projectName + ":" + filePath.getName());
		fileToInsert.setMimeType("application/file");

		// Set the parent folder.
		ArrayList<String> parentFolders = new ArrayList<String>();
		if (folderID != null) {
			parentFolders.add(folderID);
			fileToInsert.setParents(parentFolders);

		}

		try {
			drive.files().create(fileToInsert, mediaContent).execute();
		} catch (IOException e) {
			LOGGER.error("Exception adding template files: {}", e);
		}

	}

	private void shareFile(File folder, List<String> emails) {
		// TODO Auto-generated method stub

	}

	private static Permission createPermission(String email) {

		Permission newPermission1 = new Permission();
		newPermission1.setEmailAddress(email);
		newPermission1.setType("user");
		newPermission1.setRole("writer");

		return newPermission1;
	}

}
