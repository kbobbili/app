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
public class Session {
  private String sessionId;
  private String tenantId;
  private String channel;
  private String channelUserId;
  private ZonedDateTime createTime;
  private ZonedDateTime updateTime;
  private float messageCount;
  private String direction;
  private List<String> intents;
  private String application;
  private ApplicationMeta ApplicationMetaObject;
  private List<Message> messages;
  private Map<String, Object> additionalPayload;
  private Map<String, Object> tags;
}
