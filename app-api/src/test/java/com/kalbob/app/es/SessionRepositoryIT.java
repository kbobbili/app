package com.kalbob.app.es;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kalbob.app.config.ApplicationProfile;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.ResourceNotFoundException;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ApplicationProfile
public class SessionRepositoryIT {

  @Autowired
  private SessionRepository sessionRepository;
  public static DataFactory dataFactory = new DataFactory();
  private ZonedDateTime now;

  @Test
  public void saveSession() {
    Session session = getSessions(1).get(0);
    session = sessionRepository.save(session);
    assertNotNull(session.getId());
    System.out.println(session);
  }

  @Test
  public void getSession() {
    String id = "100";
    Session session = sessionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Session with id: " + id + " is not found"));
    assertNotNull(session.getId());
    System.out.println(session);
  }

  private List<Session> getSessions(int count){
    List<Session> sessions = new ArrayList<>();
    for(int s = 1; s <= count; s++) {
      Session session = new Session();
      session.setId(Integer.toString(s));
      session.setType(SessionType.TWO_WAY);
      List<Message> messages = new ArrayList<>();
      for(int m = 1; m <= 10; m++) {
        now = ZonedDateTime.now(ZoneId.of("UTC"));
        Map<String, Object> userMeta = new HashMap<>();
        userMeta.put("isActive", true);
        Map<String, Object> tags = new HashMap<>();
        tags.put("intent", "welcome");
        tags.put("lang", "en-US");
        tags.put("confidence", 1);
        Map<String, Object> eventTags = new HashMap<>();
        eventTags.put("isAnswered", true);
        Message message = new Message()
            .setTenantId("ABC")
            .setProvider(dataFactory.getItem(Arrays.asList("TROPO", "TWILIO")))
            .setProviderId("PROVIDER-" + m)
            .setChannel(Arrays.asList("TWILIO","FACEBOOK","GOOGLE", "ALEXA", "TWITTER", "STUDIO").get(dataFactory.getNumberUpTo(5)))
            .setChannelUserId("USER-" + m)
            .setText(dataFactory.getItem(Arrays
                .asList("hello! how are you", "balance", "pay bill", "report outage", "thank you")))
            .setType("SMS")
            .setAdditionalPayload(new HashMap<>())
            .setUserMeta(userMeta)
            .setDirection(dataFactory.getItem(Arrays.asList("OUT_BOUND", "IN_BOUND")))
            .setSentAt(dataFactory.getItem(Arrays.asList(now.minusDays(2).minusMinutes((count*10)-m))))
            .setApplication(dataFactory.getItem(Arrays.asList("IQ", "SM")))
            .setApplicationMeta(new ApplicationMeta().setVersion("v0").setEnvironment("dev"))
            .setIsSent(false)
            .setNotSentReason("failed due to x error")
            .setResend(true)
            .setTags(tags)
            .setEvents(Arrays.asList(new Event().setEventName("IN_PROGRESS").setTags(eventTags)));
        messages.add(message);
      }
      session.setMessages(messages);
      sessions.add(session);
    }
    return sessions;
  }
}
