package com.kalbob.app.es;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SessionSummary {
  private String tenantId;
  private String application;
  private String channel;
  private String channelUserId;
  private String sessionId;
  private ZonedDateTime startTime;
  private ZonedDateTime endTime;
  private Duration duration;
  private int messageCount;
  private Map<String, Object> firstMessage;
  private Set<String> intents;
}
