package com.kalbob.app.es;

import java.util.Map;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  IndexResponse createSession(Session session);
  IndexResponse createMessage(Message message);
  Page<Message> getMessages(Map<String, String> params, Pageable pageable);
  Page<SessionAggregate> getSessionSummaries(Map<String, String> params, Pageable pageable);

}
