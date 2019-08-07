package com.kubra.iq.chronos.entity;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SessionAggregate {
  private String sessionId;
  private String tenantId;
  private String application;
  private String channel;
  private String channelUserId;
  private ZonedDateTime startTime;
  private ZonedDateTime endTime;
  private Duration duration;
  private int messageCount;
  private Message firstMessage;
  private List<String> intents;
}
