package com.rhc.pmo.controller;

import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rhc.pmo.model.Event;
import com.rhc.pmo.model.Folder;
import com.rhc.pmo.service.AuthenticationService;
import com.rhc.pmo.service.CalendarService;
import com.rhc.pmo.service.DriveService;

@RestController
public class APIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(APIController.class);
    
    AuthenticationService authService;
    DriveService driveService;
    CalendarService calService;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody String accessToken, UriComponentsBuilder ucBuilder) throws IOException {
        LOGGER.info("Creating User {}", accessToken);
        
        authService = new AuthenticationService(accessToken);
        driveService = new DriveService(authService.getCredential());
        calService = new CalendarService(authService.getCredential());        
     
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/folders", method = RequestMethod.POST)
    public ResponseEntity<String> createFolder(@RequestBody Folder newFolder) {
        LOGGER.info("Creating folder {}", newFolder);

		try {
			driveService.initiateProjectFolder(newFolder.getClientName(), newFolder.getProjectName(),
					newFolder.getEmails());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<String>("You need to log in first, please go to localhost:8080 in your browser",
					HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			return new ResponseEntity<String>("Backend error", HttpStatus.CONFLICT);
		}

        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public ResponseEntity<String> createEvent(@RequestBody Event newEvent) {
    	LOGGER.info("Creating event {}", newEvent);

		try {
			calService.createEvent(newEvent.getLocation(), newEvent.getDescription(), newEvent.getStartDate(), newEvent.getEndDate(), newEvent.getEmails());
		} catch (NullPointerException e) {
			LOGGER.info("NullPointerException Error Logging In");
			return new ResponseEntity<String>("You need to log in first, please go to localhost:8080 in your browser",
					HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			LOGGER.info("IO Exception Error");
			return new ResponseEntity<String>("Backend error", HttpStatus.CONFLICT);
		} catch (ParseException e) {
			LOGGER.info("ParseException Error Formatting Dates");
			return new ResponseEntity<String>("Please make sure to enter dates in YYYY-MM-DD HH:MM Format", HttpStatus.UNAUTHORIZED);
		}

        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }
    
    
    
}
