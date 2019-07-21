package com.kalbob.app.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  public final RestHighLevelClient esClient;
  public final ObjectMapper objectMapper;

  public IndexResponse createMessage(Message message){
    //return index(message);
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
