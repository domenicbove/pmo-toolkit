package hello;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class AuthenticationService {

  private GoogleCredential credential;
  
  public AuthenticationService(String accessToken) {
    setCredential(new GoogleCredential().setAccessToken(accessToken));
  }

  public GoogleCredential getCredential() {
    return credential;
  }

  public void setCredential(GoogleCredential credential) {
    this.credential = credential;
  }
  
}
