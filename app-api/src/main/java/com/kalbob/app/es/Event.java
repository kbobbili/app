package com.kubra.iq.chronos.entity;

import java.time.ZonedDateTime;
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
public class Event {
  @Field(type = FieldType.Keyword)
  private String eventName;
  @Field(type = FieldType.Text)
  private String eventDetails;
  @Field(type = FieldType.Date, format = DateFormat.date_time)
  private ZonedDateTime sentAt;
  @Field(type = FieldType.Object)
  private Map<String, Object> additionalPayload;
  @Field(type = FieldType.Nested)
  private Map<String, Object> tags;
}
