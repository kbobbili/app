package com.kalbob.app.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  public final RestHighLevelClient esClient;
  public final ObjectMapper objectMapper;

  public IndexResponse createSession(Session session){
    try {
      return index("sessions_write", objectMapper.writeValueAsString(session));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private IndexResponse index(String index, String document) {
    IndexRequest request = new IndexRequest(index,"_doc")
        .source(document, XContentType.JSON);
    IndexResponse response = null;
    try {
      response = esClient.index(request, RequestOptions.DEFAULT);
    } catch(ElasticsearchException e) {
      if (e.status() == RestStatus.CONFLICT) {
        log.error("Index {} & Type {} already has a Document with id {}", request.index(), request.type(), request.id());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return response;
  }

  @Override
  public IndexResponse createMessage(Message message) {
    try {
      return index("messages_write", objectMapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Page<Message> getMessages(Map<String, String> params, Pageable pageable) {
    return null;
  }

  @Override
  public Page<SessionAggregate> getSessionSummaries(Map<String, String> params, Pageable pageable) {
    return null;
  }
}
