package com.rhc.pmo.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rhc.pmo.model.Event;
import com.rhc.pmo.service.AuthenticationService;
import com.rhc.pmo.service.CalendarService;
import com.rhc.pmo.toolkit.gdrive.DriveService;
import com.rhc.pmo.toolkit.gdrive.Folder;

@RestController
public class APIController {
    
    AuthenticationService authService;
    DriveService driveService;
    CalendarService calService;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody String accessToken, UriComponentsBuilder ucBuilder) throws IOException {
        System.out.println("Creating User " + accessToken);
        
        authService = new AuthenticationService(accessToken);
        driveService = new DriveService(authService.getCredential());
        calService = new CalendarService(authService.getCredential());        
     
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/createfolder", method = RequestMethod.POST)
    public ResponseEntity<String> createFolder(@RequestBody Folder newFolder) {
        System.out.println("Creating folder " + newFolder.toString());

		try {
			driveService.initiateProjectFolder(newFolder.getClientName(), newFolder.getProjectName(),
					newFolder.getEmails());
		} catch (NullPointerException e) {
			return new ResponseEntity<String>("You need to log in first, please go to localhost:8080 in your browser",
					HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			return new ResponseEntity<String>("Backend error", HttpStatus.CONFLICT);
		}

        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    public ResponseEntity<String> createEvent(@RequestBody Event newEvent) {
    	System.out.println("Creating event " + newEvent.toString());

		try {
			calService.createEvent(newEvent.getLocation(), newEvent.getDescription(), newEvent.getEmails());
		} catch (NullPointerException e) {
			return new ResponseEntity<String>("You need to log in first, please go to localhost:8080 in your browser",
					HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			return new ResponseEntity<String>("Backend error", HttpStatus.CONFLICT);
		}

        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }
    
    
    
}
