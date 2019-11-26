package com.kalbob.app.es;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Message {
  @Field(type = FieldType.Keyword)
  private String tenantId;
  @Field(type = FieldType.Keyword)
  private String channel;
  @Field(type = FieldType.Keyword)
  private String provider;
  @Field(type = FieldType.Keyword)
  private String providerId;
  @Field(type = FieldType.Keyword)
  private String channelUserId;
  @Field(type = FieldType.Text)
  private String text;
  @Field(type = FieldType.Object)
  private Map<String, Object> additionalPayload;
  @Field(type = FieldType.Keyword)
  private String type;
  @Field(type = FieldType.Keyword)
  private String direction;
  @Field(type = FieldType.Keyword)
  private String application;
  @Field(type = FieldType.Object)
  private ApplicationMeta applicationMeta;
  @Field(type = FieldType.Object)
  private Map<String, Object> userMeta;
  @Field(type = FieldType.Date, format = DateFormat.date_time)
  private ZonedDateTime sentAt;
  @Field(type = FieldType.Text)
  private String intent;
  @Field(type = FieldType.Nested)
  private Map<String, Object> tags;
  @Field(type = FieldType.Boolean)
  private Boolean isSent;
  @Field(type = FieldType.Text)
  private String notSentReason;
  @Field(type = FieldType.Boolean)
  private Boolean resend;
  @Field(type = FieldType.Nested)
  private List<Event> events;
  public void addEvent(Event event){
    if(this.events != null) {
      this.events.add(event);
    } else {
      this.events = new ArrayList<>();
      this.events.add(event);
    }
  }
}
