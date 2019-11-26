package com.kalbob.app.es;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(indexName = "sessions", shards = 5, replicas = 1, createIndex = false)
public class Session {
  @Id
  private String id;
  @Field(type = FieldType.Keyword)
  private SessionType type;
  @Field(type = FieldType.Nested)
  private List<Message> messages;
  public void addMessage(Message message){
    if(this.messages != null) {
      this.messages.add(message);
    } else {
      this.messages = new ArrayList<>();
      this.messages.add(message);
    }
  }
}
