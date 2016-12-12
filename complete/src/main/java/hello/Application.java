package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  
//@Bean
//String myBean() {
//    return "I'm Spring bean! RocketChat Status is: " +
//        rocketChatService.getRCStatus() ;
//}
  
//  GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//  Plus plus = new Plus.builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
//      .setApplicationName("Google-PlusSample/1.0")
//      .build();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
