package com.rhc.pmo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rhc.pmo.toolkit.gdrive.DriveService;
import com.rhc.pmo.toolkit.gdrive.Folder;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    AuthenticationService authService;
    DriveService driveService;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
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
          driveService.initiateProjectFolder(newFolder.getClientName(), newFolder.getProjectName(), newFolder.getEmails());
        } catch (Exception ex) {
           return new ResponseEntity<String>("You need to log in first", HttpStatus.UNAUTHORIZED);
        }
     
        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }

    
}
