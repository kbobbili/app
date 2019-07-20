package com.kalbob.app.es;

import java.time.ZonedDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  private String tenantId;
  private String providerId;
  private String messageId;
  private String event;
  private String eventDetails;
  private ZonedDateTime sentAt;
  private Map<String, Object> tags;
}
