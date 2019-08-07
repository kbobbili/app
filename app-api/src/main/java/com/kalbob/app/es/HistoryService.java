package com.kalbob.app.es;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {
  Session createSession(Session session);
  Session getSession(String id);
  Session addMessage(String sessionId, Message message);
  Session addEvent(String messageId, Event event);
  Page<Session> getSessions(Map<String, String> params, Pageable pageable);
  Page<JsonNode> getResults(Map<String, String> params, Pageable pageable);
  List<Message> getMessages(String sessionId);
  List<Event> getEvents(String messageId);
}
