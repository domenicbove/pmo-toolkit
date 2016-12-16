package com.rhc.pmo.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

public class DriveService {

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
				
		// Upload the templates files to the folder
		addTemplateFiles(folder.getId(), clientName, projectName);

		//Add the user permissions to the folder
		shareFile(folder, emails);

	}

	public File createFolder(String clientName, String projectName) throws IOException {
		File fileMetadata = new File();
		String date = new SimpleDateFormat("MM-yyyy").format(new Date());
		fileMetadata.setName(date + " " + clientName + " - " + projectName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File folder = drive.files().create(fileMetadata).setFields("id").execute();
		return folder;
	}

	private void addTemplateFiles(String folderID, String clientName, String projectName) {
		
		List<String> templateFileNames = new ArrayList<String>();
		templateFileNames.add("Engagement_Journal.docx");
		templateFileNames.add("Project_Dashboard.xlsx");
		templateFileNames.add("Project_Management_Plan.docx");	
		templateFileNames.add("Project_Brief.docx");	
		templateFileNames.add("Project_Kickoff_Slides.pptx");	
		templateFileNames.add("Weekly_Status_Report.docx");	
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		System.out.println("this many temp files " + templateFileNames.size());
		for (String tempName: templateFileNames) {
			java.io.File filePath = new java.io.File(classLoader.getResource("templateFiles/" + tempName).getFile());			
			FileContent mediaContent = new FileContent("application/document", filePath);
			File fileToInsert = new File();
			fileToInsert.setName(clientName + "-" + projectName + ":" + filePath.getName());
			setFileType(tempName, fileToInsert);
				
			// Set the parent folder.
			ArrayList<String> parentFolders = new ArrayList<String>();
			if (folderID != null) {
				parentFolders.add(folderID);
				fileToInsert.setParents(parentFolders);
			}
			try {
				drive.files().create(fileToInsert, mediaContent).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void shareFile(File folder, List<String> emails) throws IOException {
		JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
		    @Override
		    public void onFailure(GoogleJsonError e,
		                          HttpHeaders responseHeaders)
		            throws IOException {
		        // Handle error
		        System.err.println(e.getMessage());
		    }

		    public void onSuccess(Permission permission,
		                          HttpHeaders responseHeaders)
		            throws IOException {
		        System.out.println("Permission ID: " + permission.getId());
		    }
		};
		BatchRequest batch = drive.batch();
		
		for (String email:emails) {
			Permission perm = createPermission(email);
			drive.permissions().create(folder.getId(), perm)
	        .setFields("id")
	        .queue(batch, callback);
		}
		batch.execute();

	}
	
	private static void setFileType(String fileName, File file){

		if ((FilenameUtils.getExtension(fileName)).equals("xlsx")) {
			file.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		} else if ((FilenameUtils.getExtension(fileName)).equals("docx")) {
			file.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		} else if ((FilenameUtils.getExtension(fileName)).equals("pptx")) {
			file.setMimeType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		} else {
			file.setMimeType("application/file");
		}
		
	}
	private static Permission createPermission(String email) {

		Permission newPermission1 = new Permission();
		newPermission1.setEmailAddress(email);
		newPermission1.setType("user");
		newPermission1.setRole("writer");

		return newPermission1;
	}

}
