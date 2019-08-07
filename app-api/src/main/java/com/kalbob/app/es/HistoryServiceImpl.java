package com.kalbob.app.es;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ResourceNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

  private final SessionRepository sessionRepository;
  private final ObjectMapper objectMapper;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

  @Override
  public Session createSession(Session session) {
    return sessionRepository.save(session);
  }

  @Override
  public Session getSession(String id) {
    return sessionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Session with id: " + id + " is not found"));
  }

  @Override
  public Session addMessage(String sessionId, Message message) {
    Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
    Session session = null;
    session = sessionOptional.orElseGet(() -> new Session().setId(sessionId));
    session.addMessage(message);
    session = sessionRepository.save(session);
    return session;
  }

  @Override
  public Session addEvent(String messageId, Event event) {
    Optional<Session> sessionOptional = sessionRepository.findById(messageId);
    Session session = null;
    if(!sessionOptional.isPresent()){
      throw new ResourceNotFoundException("Message with id: " + messageId + " is not found");
    } else {
      session = sessionOptional.get();
      if(session.getType() != null && session.getType().equals(SessionType.ONE_WAY)) {
        Message message = session.getMessages().get(0);
        message.addEvent(event);
        session.getMessages().remove(0);
        session.getMessages().add(message);
        session = sessionRepository.save(session);
      }
    }
    return session;
  }

  @Override
  public List<Event> getEvents(String messageId) {
    Optional<Session> sessionOptional = sessionRepository.findById(messageId);
    Session session = null;
    List<Event> events = null;
    if(!sessionOptional.isPresent()){
      throw new ResourceNotFoundException("Message with id: " + messageId + " is not found");
    } else {
      session = sessionOptional.get();
      if(session.getType() != null && session.getType().equals(SessionType.ONE_WAY)) {
       events = session.getMessages().get(0).getEvents();
      }
    }
    return events;
  }

  @Override
  public Page<Session> getSessions(Map<String, String> params, Pageable pageable) {
    return sessionRepository.search(boolQueryBuilder(params), pageable);
  }

  @Override
  public Page<JsonNode> getResults(Map<String, String> params, Pageable pageable) {
    Page<Session> sessions = sessionRepository.search(boolQueryBuilder(params), pageable);
    List<JsonNode> results = new ArrayList<>();
    for (Session session : sessions) {
      if((session.getType() != null && session.getType().equals(SessionType.TWO_WAY)) || session.getMessages().size() > 1){
        SessionAggregate sessionAggregate = getSessionAggregate(session);
        results.add(objectMapper.convertValue(sessionAggregate, JsonNode.class));
      }else{
        results.add(objectMapper.convertValue(session, JsonNode.class));
      }
    }
    System.out.println(results);
    return new PageImpl<>(results, new PageRequest(pageable.getPageNumber(), pageable.getPageSize()),
        results.size());
  }

  @Override
  public List<Message> getMessages(String sessionId) {
    return getSession(sessionId).getMessages();
  }

  private QueryBuilder boolQueryBuilder(Map<String, String> params) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders
        .boolQuery();
    boolQueryBuilder.filter(boolFilterQuerySentAt(params));
    for (String key : params.keySet()) {
      if(!params.get(key).isEmpty() && !dateRangeFields().contains(key)) {
        if (fullTextQueryFields(params).contains(key))
          boolQueryBuilder.should(QueryBuilders.matchQuery(key, params.get(key)));
        else
          boolQueryBuilder.filter(QueryBuilders.termQuery(key, params.get(key)));
      }
    }
    System.out.println(boolQueryBuilder);
    return boolQueryBuilder;
  }

  private QueryBuilder boolFilterQuerySentAt(Map<String, String> params) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders
        .boolQuery();
    RangeQueryBuilder dateRangeBuilder = QueryBuilders
        .rangeQuery("messages.sentAt");
    dateRangeQueryBuilder(params, dateRangeBuilder);
    boolQueryBuilder.should(dateRangeBuilder);
    return boolQueryBuilder;
  }

  private void dateRangeQueryBuilder(Map<String, String> params, RangeQueryBuilder dateRangeBuilder) {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    if(params.containsKey("messages.from") && !params.get("messages.from").isEmpty()) {
      dateRangeBuilder
          .gte(ZonedDateTime.parse(params.get("messages.from"), dateTimeFormatter));
    }else {
      dateRangeBuilder
          .gte(now.minusDays(7).format(dateTimeFormatter));
    }
    if(params.containsKey("messages.to") && !params.get("messages.to").isEmpty()) {
      dateRangeBuilder
          .lte(ZonedDateTime.parse(params.get("messages.to"), dateTimeFormatter));
    }else {
      dateRangeBuilder
          .lte(now.format(dateTimeFormatter));
    }
  }

  private List<String> fullTextQueryFields(Map<String, String> params){
    List<String> textQueryFields = new ArrayList<>();
    if(params.containsKey("messages.text") && !params.get("messages.text").isEmpty()){
      textQueryFields.add("messages.text");
    }
    if(params.containsKey("messages.tags.intent") && !params.get("messages.tags.intent").isEmpty()){
      textQueryFields.add("messages.tags.intent");
    }
    if(params.containsKey("messages.events.eventDetails") && !params.get("messages.events.eventDetails").isEmpty()){
      textQueryFields.add("messages.events.eventDetails");
    }
    return textQueryFields;
  }

  private List<String> dateRangeFields(){
    return Arrays.asList("messages.from", "messages.to");
  }

  private SessionAggregate getSessionAggregate(Session session) {
    SessionAggregate sessionAggregate = new SessionAggregate();
    sessionAggregate.setSessionId(session.getId());
    List<Message> messages = session.getMessages();
    List<String> intents = new ArrayList<>();
    if(messages != null && !messages.isEmpty()) {
      Message message = messages.get(0);
      sessionAggregate.setTenantId(message.getTenantId());
      sessionAggregate.setApplication(message.getApplication());
      sessionAggregate.setChannel(message.getChannel());
      sessionAggregate.setChannelUserId(message.getChannelUserId());
      sessionAggregate.setFirstMessage(message);
      sessionAggregate.setStartTime(message.getSentAt());
      sessionAggregate.setMessageCount(messages.size());
      sessionAggregate.setEndTime(messages.get(messages.size() - 1).getSentAt());
      sessionAggregate.setDuration(
          Duration.between(sessionAggregate.getStartTime(), sessionAggregate.getEndTime()));
      for (Message msg : messages) {
        if(msg.getIntent()!=null && !msg.getIntent().isEmpty()) {
          intents.add(msg.getIntent());
        }
      }
    }
    sessionAggregate.setIntents(intents);
    return sessionAggregate;
  }
}
