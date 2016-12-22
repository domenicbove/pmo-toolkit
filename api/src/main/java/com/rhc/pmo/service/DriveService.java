package com.rhc.pmo.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(DriveService.class);
	private static File topFolder;
	private static File pmoFolder;
	private static File documFolder;
	private static File meetingsFolder;
	private static String projectName;
	private static String clientName;
	private static List<String> emails;
	private static List<String> templateFileNames = new ArrayList<String>();
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

	public void initiateProjectFolder(String client, String project, List<String> users) throws IOException {

		clientName = client;
		projectName = project;
		emails = users;

		// Create the top level folder and the subfolders
		topFolder = createFolder();
		meetingsFolder = createSubFolder("Meetings");
		pmoFolder = createSubFolder("PMO");
		documFolder = createSubFolder("Documentation");
		createSubFolder("Architecture");

		// Upload template file(s) to top level directory
		templateFileNames.add("Scope.docx");
		templateFileNames.add("Logistics.xlsx");
		templateFileNames.add("Project_Dashboard.xlsx");
		templateFileNames.add("Project_Brief.docx");
		addTemplateFiles(topFolder);
		
		//Upload file(s) to Documents Folder
		templateFileNames.add("Engagement_Journal.docx");
		addTemplateFiles(documFolder);

		//Upload file(s) to Meetings Folder
		templateFileNames.add("Project_Kickoff_Slides.pptx");
		addTemplateFiles(meetingsFolder);

		//Upload file(s) to PMO Folder
		templateFileNames.add("Weekly_Status_Report.docx");
		templateFileNames.add("Project_Management_Plan.docx");
		addTemplateFiles(pmoFolder);

		// Add the user permissions to the folder
		shareFolder();

	}

	private File createFolder() throws IOException {
		File fileMetadata = new File();
		String date = new SimpleDateFormat("yyyy-MM").format(new Date());
		fileMetadata.setName(date + clientName + " - " + projectName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		return drive.files().create(fileMetadata).setFields("id").execute();
	}

	private File createSubFolder(String name) throws IOException {
		File fileMetadata = new File();
		fileMetadata.setName(name);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		setParentFolder(fileMetadata, topFolder);
		return drive.files().create(fileMetadata).setFields("id").execute();
	}

	private void addTemplateFiles(File pFolder) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		LOGGER.info("this many temp files: {}", templateFileNames.size());
		
		for (String tempName : templateFileNames) {
			java.io.File filePath = new java.io.File(classLoader.getResource("templateFiles/" + tempName).getFile());
			FileContent mediaContent = new FileContent("application/document", filePath);
			File fileToInsert = new File();
			fileToInsert.setName(clientName + "-" + projectName + ":" + filePath.getName());
			setParentFolder(fileToInsert, pFolder);
			setGoogleFileType(tempName, fileToInsert);
			try {
				drive.files().create(fileToInsert, mediaContent).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		templateFileNames.clear();
	}

	private void shareFolder() throws IOException {
		JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
			@Override
			public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
				// Handle error
				LOGGER.error(e.getMessage());
			}
			public void onSuccess(Permission permission, HttpHeaders responseHeaders) throws IOException {
				LOGGER.info("Permission ID: {}", permission.getId());
			}
		};
		BatchRequest batch = drive.batch();
		
		for (String email : emails) {
			Permission perm = createPermission(email);
			drive.permissions().create(topFolder.getId(), perm).setFields("id").queue(batch, callback);
		}
		batch.execute();

	}
	
	private static void setMSFileType(String fileName, File file) {
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

	private static void setGoogleFileType(String fileName, File file) {
		if ((FilenameUtils.getExtension(fileName)).equals("xlsx")) {
			file.setMimeType("application/vnd.google-apps.spreadsheet");
		} else if ((FilenameUtils.getExtension(fileName)).equals("docx")) {
			file.setMimeType("application/vnd.google-apps.document");
		} else if ((FilenameUtils.getExtension(fileName)).equals("pptx")) {
			file.setMimeType("application/vnd.google-apps.presentation");
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

	private static void setParentFolder(File f, File parentFolder) {
		ArrayList<String> parentFolders = new ArrayList<String>();
		if (topFolder.getId() != null) {
			parentFolders.add(parentFolder.getId());
			f.setParents(parentFolders);
		}
	}

}
