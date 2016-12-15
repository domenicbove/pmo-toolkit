package com.rhc.pmo;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rhc.pmo.toolkit.gdrive.DriveService;
import com.rhc.pmo.toolkit.gdrive.Folder;

@RestController
public class APIController {
    
    AuthenticationService authService;
    DriveService driveService;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody String accessToken, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + accessToken);
        
        authService = new AuthenticationService(accessToken);
        driveService = new DriveService(authService.getCredential());
        
     
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

    
}