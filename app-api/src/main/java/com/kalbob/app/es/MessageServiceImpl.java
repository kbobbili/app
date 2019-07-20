package com.kalbob.app.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  public final RestHighLevelClient esClient;
  public final ObjectMapper objectMapper;

  public IndexResponse createMessage(Message message){
    return index(message);
  }

  public IndexResponse index(Message message){
    String document = null;
    try {
      document = objectMapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    IndexRequest request = new IndexRequest("messages-01","_doc")
        .source(document, XContentType.JSON);
    IndexResponse response = null;
    try {
      response = esClient.index(request, RequestOptions.DEFAULT);
    } catch(ElasticsearchException e) {
      if (e.status() == RestStatus.CONFLICT) {
        log.error("Index {} & Type {} already has a Document with id {}", request.index(), request.type(), request.id());
      }
    }catch(IOException e) {
      log.error("Failed sending the request or parsing back the response");
    }
   return response;
  }

  public Page<Message> getMessages(Map<String, String> params, Pageable pageable) {
    int from = (int) pageable.getOffset();
    int size = pageable.getPageSize();
    List<Message> messages = getMessages(params, from, size);
    return new PageImpl<>(messages, pageable, messages.size());
  }

  public List<Message> getMessages(Map<String, String> params, int from, int size){
    SearchResponse searchResponse = search(params, from, size);
    if (searchResponse != null) {
      return retrieveMessages(searchResponse);
    }
    else {
      return Collections.emptyList();
    }
  }

  private SearchResponse search(Map<String, String> params, int from , int size) {
    SearchSourceBuilder searchSourceBuilder = searchSourceBuilder(params, from, size);
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("messages_write");
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = null;
    try {
      searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return searchResponse;
  }

  private SearchSourceBuilder searchSourceBuilder(Map<String, String> params, int from, int size){
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.from(from);
    searchSourceBuilder.size(size);//fetch 100 messages for 10 sessions on the page. //but for messages endpoint; upper limit it to 1000
    searchSourceBuilder.query(queryBuilder(params));
    searchSourceBuilder.sort(sortBuilder(params));
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
    System.out.println(searchSourceBuilder);
    return searchSourceBuilder;
  }

  private SortBuilder sortBuilder(Map<String, String> params) {
    return SortBuilders
        .fieldSort("sentAt")
        .order(SortOrder.ASC);
  }

  private QueryBuilder queryBuilder(Map<String, String> params) {
    System.out.println(params);
    BoolQueryBuilder boolQueryBuilder = QueryBuilders
        .boolQuery();
    RangeQueryBuilder dateRangeBuilder = QueryBuilders
        .rangeQuery("sentAt");
    if(params.containsKey("from") && !params.get("from").isEmpty()) {
      dateRangeBuilder
          .gte(ZonedDateTime.parse(params.get("from"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")));
    }else {
      dateRangeBuilder
          .gte(ZonedDateTime.now(ZoneId.of("UTC")).minusMonths(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")));
    }
    if(params.containsKey("to") && !params.get("to").isEmpty()) {
      dateRangeBuilder
          .lte(ZonedDateTime.parse(params.get("to"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")));
    }else {
      dateRangeBuilder
          .lte(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")));
    }
    boolQueryBuilder.filter(dateRangeBuilder);
    for (String key : params.keySet()) {
      if(!params.get(key).isEmpty() && !dateRangeFields().contains(key) && !paginationFields().contains(key)) {
        if (fullTextQueryFields().contains(key))
          boolQueryBuilder.must(QueryBuilders.matchQuery(key, params.get(key)));
        else
          boolQueryBuilder.filter(QueryBuilders.termQuery(key, params.get(key)));
      }
    }
    System.out.println(boolQueryBuilder);
    return boolQueryBuilder;
  }

  private List<String> fullTextQueryFields(){
    return Arrays.asList("message.text", "tags.intent");
  }

  private List<String> dateRangeFields(){
    return Arrays.asList("from", "to");
  }

  private List<String> paginationFields(){
    return Arrays.asList("page","size","sort");
  }

  private List<Message> retrieveMessages(SearchResponse searchResponse) {
    RestStatus status = searchResponse.status();
    TimeValue took = searchResponse.getTook();
    SearchHits hits = searchResponse.getHits();
    //long totalHits = hits.getTotalHits();
    SearchHit[] searchHits = hits.getHits();
    List<Message> messages = new ArrayList<>();
    for (SearchHit hit : searchHits) {
      String index = hit.getIndex();
      String id = hit.getId();
      float score = hit.getScore();
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      messages.add(objectMapper.convertValue(sourceAsMap, Message.class));
    }
    System.out.println("Total Messages Count "+messages.size());
    System.out.println("Messages "+messages);
    return messages;
  }

  public Page<SessionSummary> getSessionSummaries(Map<String, String> params, Pageable pageable) {
    int from = (int) pageable.getOffset();
    int size = pageable.getPageSize();
    List<SessionSummary> sessionSummaries = getSessionSummaries(params, from, size);
    return new PageImpl<>(sessionSummaries, pageable, sessionSummaries.size());
  }

  public List<SessionSummary> getSessionSummaries(Map<String, String> params, int from, int size){
    List<SessionSummary> sessionSummaries = new ArrayList<>();
    int retrievedSessionsCount = 0;
    int msgsSize = size * 10;
    while (retrievedSessionsCount < size && (from+msgsSize) < 10000) {
      SearchResponse searchResponse = search(params, from, msgsSize);
      if (searchResponse != null) {
        List<SessionSummary> interimSessionSummaries = retrieveSessionSummaries(retrieveMessages(searchResponse));
        retrievedSessionsCount += interimSessionSummaries.size();
        if(retrievedSessionsCount == size) {
          sessionSummaries.addAll(interimSessionSummaries);
        }else if(retrievedSessionsCount > size){
          sessionSummaries.addAll(interimSessionSummaries.subList(0, interimSessionSummaries.size()-(retrievedSessionsCount-size)));
        }else {
          sessionSummaries.addAll(interimSessionSummaries);
          from = from + msgsSize;
          msgsSize = (size - retrievedSessionsCount) * size;
        }
      }
    }
    return sessionSummaries;
  }

  public List<SessionSummary> retrieveSessionSummaries(List<Message> messages) {
    Map<String, SessionSummary> sessionSummaryMap = new LinkedHashMap<>();
    for (Message message : messages) {
      SessionSummary sessionSummary = sessionSummaryMap.get(message.getSessionId());
      if (sessionSummary == null) {
        Object intent = message.getTags().get("intent");
        sessionSummary = new SessionSummary()
            .setTenantId(message.getTenantId())
            .setApplication(message.getApplication())
            .setChannel(message.getChannel())
            .setChannelUserId(message.getChannelUserId())
            .setSessionId(message.getSessionId())
            .setStartTime(message.getSentAt())
            .setEndTime(message.getSentAt())
            .setDuration(Duration.ZERO)
            .setFirstMessage(message.getMessage())
            .setIntents(intent != null ?
                new HashSet<>(Arrays.asList(intent.toString())) : new HashSet<>())
            .setMessageCount(1);
        sessionSummaryMap.put(message.getSessionId(), sessionSummary);
      } else {
        if (message.getSentAt().isBefore(sessionSummary.getStartTime())) {
          sessionSummary.setStartTime(message.getSentAt());
          sessionSummary.setDuration(
              Duration.between(sessionSummary.getStartTime(), sessionSummary.getEndTime()));
          sessionSummary.setFirstMessage(message.getMessage());
        } else if (message.getSentAt().isAfter(sessionSummary.getEndTime())) {
          sessionSummary.setEndTime(message.getSentAt());
          sessionSummary.setDuration(
              Duration.between(sessionSummary.getStartTime(), sessionSummary.getEndTime()));
        }
        Object intent = message.getTags().get("intent");
        if (intent != null) {
          sessionSummary.getIntents().add(intent.toString());
        }
        sessionSummary.setMessageCount(sessionSummary.getMessageCount() + 1);
        sessionSummaryMap.put(message.getSessionId(), sessionSummary);
      }
    }
    List<SessionSummary> sessionSummaries = new ArrayList<>(sessionSummaryMap.values());
    System.out.println("Total Sessions Count "+sessionSummaries.size());
    System.out.println("Sessions "+sessionSummaries);
    return sessionSummaries;
  }



}
