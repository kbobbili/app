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
  private String eventName;
  private String eventDetails;
  private ZonedDateTime sentAt;
  private Map<String, Object> additionalPayload;
  private Map<String, Object> tags;
}
