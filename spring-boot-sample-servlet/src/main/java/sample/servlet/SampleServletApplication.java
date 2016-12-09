/*
 * Copyright 2012-2016 the original author or authors. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package sample.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

@SpringBootConfiguration
@EnableAutoConfiguration(
  exclude = {org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
@WebServlet("/hello")
public class SampleServletApplication extends SpringBootServletInitializer {


  /** Application name. */
  private static final String APPLICATION_NAME = "Drive API Java Quickstart";

  /** Directory to store user credentials for this application. */
  private static final java.io.File DATA_STORE_DIR =
    new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart");

  /** Global instance of the {@link FileDataStoreFactory}. */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /** Global instance of the HTTP transport. */
  private static HttpTransport HTTP_TRANSPORT;
  /**
   * Global instance of the scopes required by this quickstart.
   *
   * If modifying these scopes, delete your previously saved credentials at
   * ~/.credentials/drive-java-quickstart
   */
  private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

  static {
    try {
      HTTP_TRANSPORT = new NetHttpTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }
  
  public static Credential credential;
  
  public String getLoginUrl() throws IOException {
    // Load client secrets.
    InputStream in = SampleServletApplication.class.getResourceAsStream("/client_secret.json");
    GoogleClientSecrets clientSecrets =
      GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    final GoogleAuthorizationCodeFlow flow =
      new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();

    final VerificationCodeReceiver receiver = new LocalServerReceiver();
    final String userId = "user";
    
    credential = flow.loadCredential(userId);
    System.out.println("credential: " + credential);
    
    if ((credential != null) && ((credential.getRefreshToken() != null)
      || (credential.getExpiresInSeconds().longValue() > 60L))) {
      
      System.out.println("If credential is valid");
      return null;
    } else {
      
      final String redirectUri = receiver.getRedirectUri();

      System.out.println("redirectUri: " + redirectUri);

      AuthorizationCodeRequestUrl authorizationUrl =
        flow.newAuthorizationUrl().setRedirectUri(redirectUri);
      
      System.out.println("authorizationUrl" + authorizationUrl);
      
      Thread t = new Thread(){
          public void run() {
            try {
              login(receiver, flow, userId, redirectUri);
              System.out.println("in thread");
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
      };
      t.start();
      return authorizationUrl.toURL().toString();
    }
    
  }
  
  public void login(VerificationCodeReceiver receiver, GoogleAuthorizationCodeFlow flow, String userId, String redirectUri) throws IOException {
    String code = receiver.waitForCode();
    System.out.println("code" + code);
    
    TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
    
    credential = flow.createAndStoreCredential(response, userId);
    receiver.stop();
  }

  public static Credential authorize() throws IOException {
    // Load client secrets.
    InputStream in = SampleServletApplication.class.getResourceAsStream("/client_secret.json");
    GoogleClientSecrets clientSecrets =
      GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
      new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();

    VerificationCodeReceiver receiver = new LocalServerReceiver();
    String userId = "user";
    
    try {
      Credential credential = flow.loadCredential(userId);
      System.out.println("credential: " + credential);
      
      if ((credential != null) && ((credential.getRefreshToken() != null)
        || (credential.getExpiresInSeconds().longValue() > 60L))) {
        
        System.out.println("cin if statement: ");
        return credential;
      }
      
      String redirectUri = receiver.getRedirectUri();
      System.out.println("redirectUri: " + redirectUri);

      AuthorizationCodeRequestUrl authorizationUrl =
        flow.newAuthorizationUrl().setRedirectUri(redirectUri);
      
      System.out.println("authorizationUrl" + authorizationUrl);

      //return authorizationUrl.toURL().toString();
      // TODO send this url to browser
      // onAuthorization(authorizationUrl);

      String code = receiver.waitForCode();
      System.out.println("code" + code);
      
      TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
      
      return flow.createAndStoreCredential(response, userId);
    } finally {
      receiver.stop();
    }

  }

  /**
   * Build and return an authorized Drive client service.
   * 
   * @return an authorized Drive client service
   * @throws IOException
   */
  public static Drive getDriveService(Credential credential) throws IOException {
    System.out.println("in getDriveService");
    return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME).build();
  }



  @SuppressWarnings("serial")
  @Bean
  public Servlet dispatcherServlet() {

    return new HttpServlet() {
      @Override
      public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        
        String url = getLoginUrl();
        if (url != null) {
          resp.sendRedirect(url);
        } else {
          System.out.println("in else");
          
          Drive d = getDriveService(credential);
          
          resp.setContentType("text/plain");
          resp.getWriter().append("Hello World\n");
          resp.getWriter().append(d.getRootUrl());
          
        }

      }

    };
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SampleServletApplication.class);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(SampleServletApplication.class, args);
  }

}
