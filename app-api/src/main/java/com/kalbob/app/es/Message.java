package com.kalbob.app.es;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Message {
  private String tenantId;
  private String channel;
  private String provider;
  private String providerId;
  private String channelUserId;
  private String text;
  private Map<String, Object> additionalPayload;
  private String type;
  private String direction;
  private String application;
  private ApplicationMeta applicationMeta;
  private Map<String, Object> userMeta;
  private ZonedDateTime sentAt;
  private String intent;
  private Map<String, Object> tags;
  private Boolean isSent;
  private String notSentReason;
  private Boolean resend;
  private List<Event> events;
}
