package com.kalbob.app.es;

import java.time.ZonedDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Event {
  private String eventName;
  private String eventDetails;
  private ZonedDateTime sentAt;
  private Map<String, Object> additionalPayload;
  private Map<String, Object> tags;
}
