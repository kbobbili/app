package com.kalbob.app.es;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
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
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ElasticSearchIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private RestHighLevelClient client = new RestHighLevelClient(
      RestClient.builder(
          new HttpHost("localhost", 9200, "http")));
  private ObjectMapper objectMapper = new ObjectMapper();
  public static DataFactory dataFactory = new DataFactory();

  @BeforeEach
  public void beforeEach() throws Exception {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
  }

  @AfterEach
  public void afterEach() throws Exception {
  }

  @Test
  public void test() throws JsonProcessingException {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    System.out.println(now);//2019-07-19T00:00:00.000Z[UTC]
    System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(now));
    System.out.println(ZonedDateTime.parse(now.toString(), DateTimeFormatter.ISO_ZONED_DATE_TIME));
    //System.out.println(objectMapper.writeValueAsString(getMessages(1)));
    searchSourceBuilder();
  }

  @Test
  public void ping() throws IOException {
    assertTrue(client.ping(RequestOptions.DEFAULT));
  }

  @Test
  public void health() throws IOException {
    ClusterHealthRequest request = new ClusterHealthRequest("tweets_*");
    request.timeout("50s");
    request.masterNodeTimeout("20s");
    request.waitForYellowStatus();
    request.waitForNoRelocatingShards(true);
    request.waitForNoInitializingShards(true);
    request.waitForNodes("le(1)");
    request.waitForActiveShards(1);
    request.waitForEvents(Priority.NORMAL);
    request.level(ClusterHealthRequest.Level.SHARDS);
    ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);
    System.out.println(response);
    assertTrue(response.getActivePrimaryShards() > 0);
    Map<String, ClusterIndexHealth> indices = response.getIndices();
    System.out.println(indices);
    assertTrue(indices.size() > 0);
  }

  private List<HistMessage> getMessages(int count){
    List<HistMessage> messages = new ArrayList<>();
    for(int i = 1; i <=count; i++) {
      int id = 0;
      if(count <=5 ) {
        id = dataFactory.getNumberUpTo(count) + 1; //[1,count]
      } else {
        id = dataFactory.getNumberUpTo(count/5) + 1; //[1,count/5]
      }
      Map<String, Object> msgPayload = new HashMap<>();
      msgPayload.put("text", dataFactory.getItem(Arrays
          .asList("hello! how are you", "balance", "pay bill", "report outage", "thank you")));
      msgPayload.put("user", new User().setUserId("USER-" + id));
      Map<String, Object> tags = new HashMap<>();
      tags.put("intent", "welcome");
      tags.put("lang", "en-US");
      tags.put("confidence", 1);
      HistMessage message = new HistMessage()
          .setTenantId("ABC")
          .setApplication("IQ")
          .setChannel(Arrays.asList("TWILIO","FACEBOOK","GOOGLE").get(dataFactory.getNumberUpTo(2)))
          .setChannelUserId("13343329276")
          .setSessionId("SESSION-" + id)
          .setMessage(msgPayload)
          .setMessageDirection(dataFactory.getItem(Arrays.asList("IN_BOUND")))
          .setSentAt(ZonedDateTime.now(ZoneId.of("UTC")))
          .setApplicationMeta(new ApplicationMeta().setVersion("v0"))
          .setTest("test")
          .setTags(tags);
      messages.add(message);
    }
    return messages;
  }

  @Test
  public void index() throws IOException {
    HistMessage message = getMessages(1).get(0);
    String document = objectMapper.writeValueAsString(message);
    IndexRequest request = new IndexRequest("messages-01","_doc")
        .source(document, XContentType.JSON);
    IndexResponse response = null;
    try {
      response = client.index(request, RequestOptions.DEFAULT);
    } catch(ElasticsearchException e) {
      if (e.status() == RestStatus.CONFLICT) {
        logger.error("Index {} & Type {} already has a Document with id {}", request.index(), request.type(), request.id());
      }
    }
    assertEquals(response.getResult(), DocWriteResponse.Result.CREATED);
    //assertGet
  }

  @Test
  public void bulkIndex() throws IOException {
    List<HistMessage> messages = getMessages(100);
    BulkProcessor.Listener bulkListener = getBulkListener();
    BulkProcessor.Builder bulkProcessorBuilder = BulkProcessor.builder(
        (request, listener) ->
            client.bulkAsync(request, RequestOptions.DEFAULT, listener),
        bulkListener);
    bulkProcessorBuilder.setBulkActions(500);
    bulkProcessorBuilder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
    bulkProcessorBuilder.setConcurrentRequests(0);
    bulkProcessorBuilder.setFlushInterval(TimeValue.timeValueSeconds(10L));
    bulkProcessorBuilder.setBackoffPolicy(BackoffPolicy
        .constantBackoff(TimeValue.timeValueSeconds(1L), 3));
    BulkProcessor bulkProcessor = bulkProcessorBuilder.build();
    for(HistMessage message: messages) {
      bulkProcessor.add(new IndexRequest("messages-01", "_doc")
          .source(objectMapper.writeValueAsString(message), XContentType.JSON));
    }
    try {
      boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //assertGetCount
  }

  private Listener getBulkListener() {
    return new BulkProcessor.Listener() {
      @Override
      public void beforeBulk(long executionId, BulkRequest request) {
        int numberOfActions = request.numberOfActions();
        logger.debug("Executing bulk [{}] with {} requests",
            executionId, numberOfActions);
      }

      @Override
      public void afterBulk(long executionId, BulkRequest request,
          BulkResponse response) {
        if (response.hasFailures()) {
          logger.warn("Bulk [{}] executed with failures", executionId);
        } else {
          logger.debug("Bulk [{}] completed in {} milliseconds",
              executionId, response.getTook().getMillis());
        }
      }

      @Override
      public void afterBulk(long executionId, BulkRequest request,
          Throwable failure) {
        logger.error("Failed to execute bulk", failure);
      }
    };
  }

  @Test
  public void search() {
    SearchSourceBuilder searchSourceBuilder = searchSourceBuilder();
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("messages_write");
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = null;
    try {
      searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(searchResponse!=null) handleResponse(searchResponse);
  }

  private void handleResponse(SearchResponse searchResponse) {
    RestStatus status = searchResponse.status();
    TimeValue took = searchResponse.getTook();
    SearchHits hits = searchResponse.getHits();
    long totalHits = hits.getTotalHits();
    SearchHit[] searchHits = hits.getHits();
    List<HistMessage> messages = new ArrayList<>();
    for (SearchHit hit : searchHits) {
      String index = hit.getIndex();
      String id = hit.getId();
      float score = hit.getScore();
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      messages.add(objectMapper.convertValue(sourceAsMap, HistMessage.class));
    }
    System.out.println("Total Messages Count "+messages.size());
    System.out.println("Messages "+messages);
    buildSessionSummaries(messages);
  }

  private void buildSessionSummaries(List<HistMessage> messages) {
    Map<String, SessionSummary> sessionSummaryMap = new LinkedHashMap<>();
    for (HistMessage message : messages) {
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
    List<SessionSummary> sessionSummaries = sessionSummaryMap.values().stream()
        .sorted((Comparator.comparing(SessionSummary::getStartTime)).reversed())
        .collect(Collectors.toList());
    System.out.println("Total Sessions Count "+sessionSummaries.size());
    System.out.println("Sessions "+sessionSummaries);
    /*if(sessionSummaries.size()<10){
      search();
    }*/
  }

  public Page<SessionSummary> getSessionsPaged(List<SessionSummary> sessions, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = (start + pageable.getPageSize()) > sessions.size()
        ? sessions.size() : (start + pageable.getPageSize());
    return new PageImpl<>(sessions.subList(start, end), pageable,
        sessions.size());
  }

  private SearchSourceBuilder searchSourceBuilder(){
    String url = "?tenantId=ABC&channelUserId=13343329276&from=2019-07-15T00:00:00.000Z&to=2019-07-20T00:00:00.000Z&page=0&size=10";//&channel=TWILIO&message.text=pay
    List<NameValuePair> listOfParams = new ArrayList<>();
    try {
      listOfParams = URLEncodedUtils.parse(new URI(url), Charset.forName("UTF-8"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    Map<String, String> params = new HashMap<>();
    for (NameValuePair param : listOfParams) {
      params.put(param.getName(), param.getValue());
    }
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.from(Integer.parseInt(params.get("page")));
    searchSourceBuilder.size(Integer.parseInt(params.get("size"))*2);//fetch 100 messages for 10 sessions on the page. //but for messages endpoint; upper limit it to 1000
    params.remove("page");params.remove("size");
    searchSourceBuilder.query(queryBuilder(params));
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
    System.out.println(searchSourceBuilder);
    return searchSourceBuilder;
  }

  private QueryBuilder queryBuilder(Map<String, String> params) {
    System.out.println(params);
    BoolQueryBuilder boolQueryBuilder = QueryBuilders
        .boolQuery();
    RangeQueryBuilder dateRangeBuilder = QueryBuilders
        .rangeQuery("sentAt");
    if(params.containsKey("from") && !params.get("from").isEmpty()) {
      dateRangeBuilder
          .gte(ZonedDateTime.parse(params.get("from"), DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }
    if(params.containsKey("to") && !params.get("to").isEmpty()) {
      dateRangeBuilder
          .lte(ZonedDateTime.parse(params.get("to"), DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }
    boolQueryBuilder.filter(dateRangeBuilder);
    for (String key : params.keySet()) {
      if(!params.get(key).isEmpty() && !dateRangeFields().contains(key)) {
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


}
