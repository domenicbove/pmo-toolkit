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

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody String accessToken, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + accessToken);
        
        AuthenticationService as = new AuthenticationService(accessToken);
        DriveService ds = new DriveService(as.getCredential());
        
        try {
          ds.createFolder("client1", "brms");
          System.out.println("hope the folder was created");
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
     
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    

    
}
