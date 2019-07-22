package com.kalbob.app.es;

import static org.elasticsearch.common.xcontent.NamedXContentRegistry.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
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
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Slf4j
public class ElasticSearchIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private RestHighLevelClient client = new RestHighLevelClient(
      RestClient.builder(
          new HttpHost("localhost", 9200, "http")));
  private ObjectMapper objectMapper = new ObjectMapper();
  public static DataFactory dataFactory = new DataFactory();
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
  private ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

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
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
    System.out.println(now);//2019-07-19T00:00:00.000Z[UTC]
    System.out.println(dateTimeFormatter.format(now));
    System.out.println(ZonedDateTime.parse(now.toString(), DateTimeFormatter.ISO_ZONED_DATE_TIME));
    System.out.println(now.minusDays(7).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    System.out.println(now.minusDays(7).format(dateTimeFormatter));
    now.minusMonths(3).format(dateTimeFormatter);
    //System.out.println(objectMapper.writeValueAsString(getMessagesCollapsedToSessions(1)));
    //searchSourceBuilder();

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

  @Test
  public void index() throws IOException {
    Session session = getSessions(1).get(0);
    String document = objectMapper.writeValueAsString(session);
    IndexRequest request = new IndexRequest("s_w","_doc")
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

  private List<Message> getMessages(int count) {
    List<Message> messages = new ArrayList<>();
    for(int i = 1; i <= count; i++) {
      Map<String, Object> userMeta = new HashMap<>();
      userMeta.put("isActive", true);
      Map<String, Object> eventTags = new HashMap<>();
      eventTags.put("isAnswered", true);
      Message message = new Message()
          .setTenantId("ABC")
          .setProvider("TROPO")
          .setProviderId("PROVIDER-" + i)
          .setChannel(Arrays.asList("TWILIO","FACEBOOK","GOOGLE").get(dataFactory.getNumberUpTo(2)))
          .setChannelUserId("USER-" + i)
          .setText(dataFactory.getItem(Arrays
              .asList("hello! how are you", "balance", "pay bill", "report outage", "thank you")))
          .setType("SMS")
          .setAdditionalPayload(new HashMap<>())
          .setUserMeta(userMeta)
          .setDirection(dataFactory.getItem(Arrays.asList("OUT_BOUND")))
          .setSentAt(now.minusMinutes((count*10)-i))
          .setApplication("SM")
          .setApplicationMeta(new ApplicationMeta().setVersion("v0").setEnvironment("dev"))
          .setIsSent(false)
          .setNotSentReason("failed due to x error")
          .setResend(true);
          //.setTags(null)
          //.setEvents(Arrays.asList(new Event().setEventName("IN_PROGRESS").setTags(eventTags)));
      messages.add(message);
    }
    return messages;
  }

  private List<Session> getSessions(int count){
    List<Session> sessions = new ArrayList<>();
    for(int s = 1; s <= count; s++) {
      Session session = new Session();
      session.setSessionId("SESSION-" + s);
      List<Message> messages = new ArrayList<>();
      for(int m = 1; m <= 10; m++) {
        Map<String, Object> userMeta = new HashMap<>();
        userMeta.put("isActive", true);
        Map<String, Object> tags = new HashMap<>();
        tags.put("intent", "welcome");
        tags.put("lang", "en-US");
        tags.put("confidence", 1);
        Message message = new Message()
            .setTenantId("ABC")
            .setChannel(
                Arrays.asList("TWILIO", "FACEBOOK", "GOOGLE").get(dataFactory.getNumberUpTo(2)))
            .setChannelUserId("USER-" + m)
            .setText(dataFactory.getItem(Arrays
                .asList("hello! how are you", "balance", "pay bill", "report outage", "thank you")))
            .setType("SMS")
            .setAdditionalPayload(new HashMap<>())
            .setUserMeta(userMeta)
            .setDirection(dataFactory.getItem(Arrays.asList("OUT_BOUND")))
            .setSentAt(now.minusMinutes((count * 10) - m))
            .setApplication("SM")
            .setApplicationMeta(new ApplicationMeta().setVersion("v0").setEnvironment("dev"))
            .setTags(tags);
        messages.add(message);
      }
      session.setMessages(messages);
      sessions.add(session);
    }
    return sessions;
  }

  @Test
  public void updateById() throws IOException {
    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.index("s");
    updateRequest.type("_doc");
    updateRequest.id("1");
    Map<String, Object> params = new HashMap<>();
    params.put("a","CodeUpdateById");
    //params.put("b", "CodeUpdateById");
    //String message = "{\"test\":\"test\"}";
    params.put("message", new SmallMessage().setText("CodeUpdateById"));
    String payload = objectMapper.writeValueAsString(params);
    params.put("message", payload);
    XContentBuilder b = XContentFactory.jsonBuilder().prettyPrint();
    try (XContentParser p = XContentFactory.xContent(XContentType.JSON)
        .createParser(EMPTY, DeprecationHandler.THROW_UNSUPPORTED_OPERATION, payload)) {
      b.copyCurrentStructure(p);
      /*updateRequest.doc(jsonBuilder()
          .startObject()
          .field("sessionId", "CodeUpdateById")
          .array("arr", "a","b","c")
          .endObject());*/
      updateRequest.doc(b);
      client.update(updateRequest, RequestOptions.DEFAULT);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void update() throws IOException {
    Map<String, Object> params = new HashMap<>();
    params.put("a","CodeUpdate1");
    params.put("b", "CodeUpdate1");
    //params.put("message", objectMapper.writeValueAsString(message));
    //params.put("message", objectMapper.convertValue(message, JsonNode.class));
    //params.put("message", objectMapper.writeValueAsString(objectMapper.convertValue(message, JsonNode.class)));
    UpdateRequest request = new UpdateRequest("s","1");
    //params.put("message", objectMapper.writeValueAsString(new SmallMessage().setText("CodeUpdate1")));//"mapper_parsing_exception object mapping for [messages] tried to parse field [null] as object, but found a concrete value
    params.put("message", new SmallMessage().setText("CodeUpdate1"));//cannot write xcontent for unknown value
    request.script(
        new Script(
            ScriptType.INLINE, "painless",
            //"if (ctx._source.containsKey(\"messages\")) {ctx._source.messages.add(params.message);ctx._source.b=params.b} else {ctx._source.messages = [params.message]}",
            //"ctx._source.a=params.a",
            //"ctx._source.messages[0]=params.message",
            //"ctx._source.messages.add(params.message)",
            //"ctx._source.messages[0].text=params.a",
            //"ctx._source.a=\"CodeUpdate2\"",
            //ctx._source.messages[0] = \"Console3\" //"mapper_parsing_exception object mapping for [messages] tried to parse field [null] as object, but found a concrete value
            //"ctx._source.messages[0]={\"text\":\"CodeUpdate1\"}", //special chars { exception
            "ctx._source.messages.add(params.message)",
            params));
    System.out.println(request);
    client.update(request, RequestOptions.DEFAULT);
    //client.updateByQueryAsync(request, RequestOptions.DEFAULT, asyncUpdateByQueryListener());
    /*long updatedDocs = bulkResponse.getUpdated();
    System.out.println(bulkResponse);*//*
    assertEquals(updatedDocs, 1);*/
    System.out.println("end");
  }

  @Test
  public void updateByQuery() throws IOException {
    UpdateByQueryRequest request = new UpdateByQueryRequest("s");
    request.setQuery(new TermQueryBuilder("sessionId", "SESSION-1"));
    Map<String, Object> params = new HashMap<>();
    //params.put("a","CodeUpdateByQuery");
    String msgText = objectMapper.writeValueAsString(new SmallMessage().setText("CodeUpdateByQuery"));
    XContentBuilder message = XContentFactory.jsonBuilder().prettyPrint();
    try (XContentParser p = XContentFactory.xContent(XContentType.JSON)
        .createParser(EMPTY, DeprecationHandler.THROW_UNSUPPORTED_OPERATION, msgText)) {
      message.copyCurrentStructure(p);
    }
    params.put("m", message.contentType());

    request.setScript(
        new Script(
            ScriptType.INLINE, "painless",
            //"if (ctx._source.containsKey(\"messages\")) {ctx._source.messages.add(params.message);ctx._source.b=params.b} else {ctx._source.messages = [params.message]}",
            "ctx._source.x=params.m",
            //"ctx._source.messages[0]=params.message",
            //"ctx._source.messages.add(params.message)",
            //"ctx._source.messages[0].text=params.a",
            //"ctx._source.a=\"Code2\"",
            //ctx._source.messages[0] = \"Console3\" //"mapper_parsing_exception object mapping for [messages] tried to parse field [null] as object, but found a concrete value
            //"ctx._source.messages[0]={\"text\":\"Code1\"}", //special chars { exception
            //"ctx._source.messages.add(params.m)",
            params));
    request.setConflicts("proceed");
    request.setSize(1);
    request.setBatchSize(1);
    request.setRefresh(true);
    request.setScroll(TimeValue.timeValueMinutes(100));
    client.updateByQuery(request, RequestOptions.DEFAULT);
  }

  @Test
  public void updateByQuery2() throws IOException {
    Map<String, Object> params = new HashMap<>();
    //params.put("a","Code1");
    //params.put("b", "Code1");
    //params.put("message", objectMapper.writeValueAsString(message));
    //params.put("message", objectMapper.convertValue(message, JsonNode.class));
    //params.put("message", objectMapper.writeValueAsString(objectMapper.convertValue(message, JsonNode.class)));
    UpdateByQueryRequest request = new UpdateByQueryRequest("s");
    request.setQuery(new TermQueryBuilder("sessionId", "SESSION-1"));
    //params.put("message", objectMapper.writeValueAsString(new SmallMessage().setText("Code1")));//"mapper_parsing_exception object mapping for [messages] tried to parse field [null] as object, but found a concrete value
    params.put("message", objectMapper.writeValueAsBytes(new SmallMessage().setText("Code1")));//cannot write xcontent for unknown value
    request.setScript(
        new Script(
            ScriptType.INLINE, "painless",
            //"if (ctx._source.containsKey(\"messages\")) {ctx._source.messages.add(params.message);ctx._source.b=params.b} else {ctx._source.messages = [params.message]}",
            //"ctx._source.a=params.a",
            "ctx._source.messages[0]=params.message",
            //"ctx._source.messages.add(params.message)",
            //"ctx._source.messages[0].text=params.a",
            //"ctx._source.a=\"Code2\"",
            //ctx._source.messages[0] = \"Console3\" //"mapper_parsing_exception object mapping for [messages] tried to parse field [null] as object, but found a concrete value
            //"ctx._source.messages[0]={\"text\":\"Code1\"}", //special chars { exception
            //"ctx._source.messages.add(params.message)",
            params));
    request.setConflicts("proceed");
    request.setSize(1);
    request.setBatchSize(1);
    request.setRefresh(true);
    request.setScroll(TimeValue.timeValueMinutes(100));
    System.out.println(request);
    client.updateByQuery(request, RequestOptions.DEFAULT);
    //client.updateByQueryAsync(request, RequestOptions.DEFAULT, asyncUpdateByQueryListener());
    /*long updatedDocs = bulkResponse.getUpdated();
    System.out.println(bulkResponse);*//*
    assertEquals(updatedDocs, 1);*/
    System.out.println("end");
  }

  private ActionListener<BulkByScrollResponse> asyncUpdateByQueryListener(){
    return new ActionListener<BulkByScrollResponse>() {
      @Override
      public void onResponse(BulkByScrollResponse bulkResponse) {
        long updatedDocs = bulkResponse.getUpdated();
        System.out.println("bulkResponse "+bulkResponse);
      }

      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
      }
    };
  }

  @Test
  public void bulkIndex() throws IOException {
    List<Session> sessions = getSessions(30);
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
    for(Session session: sessions) {
      bulkProcessor.add(new IndexRequest("s_w", "_doc")
          .source(objectMapper.writeValueAsString(session), XContentType.JSON));
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
    String url = "?page=0&size=10&sort=sentAt,asc";
    List<NameValuePair> listOfParams = new ArrayList<>();
    try {
      listOfParams = URLEncodedUtils.parse(new URI(url), Charset.forName("UTF-8"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    Map<String, String> params = new HashMap<>();
    Map<String, String> sortParams = new HashMap<>();
    int pageNumber = 0;
    int size = 10;
    for (NameValuePair param : listOfParams) {
      String paramKey = param.getName();
      String paramValue = param.getValue();
      if(paramKey.equals("sort")){
        sortParams.put(paramValue.substring(0, paramValue.indexOf(",")), paramValue.substring(paramValue.indexOf(",")+1));
      }else if(paginationFields().contains(paramKey)){
        if(params.get("page") != null && !params.get("page").isEmpty()){
          pageNumber = Integer.parseInt(params.get("page"));
        }
        if(params.get("size") != null && !params.get("size").isEmpty()){
          size = Integer.parseInt(params.get("size"));
        }
      } else {
        params.put("messages." + paramKey, paramValue);
      }
    }
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    addPaginationInfo(pageNumber, size, searchSourceBuilder);
    addSortBuilders(sortParams, searchSourceBuilder);
    addQueryBuilders(params, searchSourceBuilder);
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("s_w");
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = null;
    try {
      System.out.println("searchRequest "+searchRequest);
      searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      System.out.println("searchResponse "+searchResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<JsonNode> results = new ArrayList<>();
    if(searchResponse!=null) {
      results = handleResponse(searchResponse);
    }
    Page<JsonNode> resultsWithPageMeta = new PageImpl<>(results, new PageRequest(pageNumber, size),
        results.size());
    System.out.println(resultsWithPageMeta);
  }

  private void addPaginationInfo(int pageNumber, int size, SearchSourceBuilder searchSourceBuilder) {
    searchSourceBuilder.from(pageNumber * size);
    searchSourceBuilder.size(size);
  }

  private void addQueryBuilders(Map<String, String> params, SearchSourceBuilder searchSourceBuilder) {
    searchSourceBuilder.query(queryBuilder(params));
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
  }

  private void addSortBuilders(Map<String, String> sortParams, SearchSourceBuilder searchSourceBuilder) {
    for(String sortKey: sortParams.keySet()){
      if(sortParams.get(sortKey).equals("asc")){
        searchSourceBuilder.sort(new FieldSortBuilder("messages." + sortKey).order(SortOrder.ASC));
      }else if(sortParams.get(sortKey).equals("desc")){
        searchSourceBuilder.sort(new FieldSortBuilder("messages." + sortKey).order(SortOrder.DESC));
      }else{
        searchSourceBuilder.sort(new FieldSortBuilder("messages." + sortKey));
      }
    }
  }

  private QueryBuilder queryBuilder(Map<String, String> params) {
    System.out.println(params);
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
    if(params.containsKey("messages.from") && !params.get("messages.from").isEmpty()) {
      dateRangeBuilder
          .gte(ZonedDateTime.parse(params.get("messages.from"), dateTimeFormatter));
    }else {
      dateRangeBuilder
          .gte(now.minusMonths(6).format(dateTimeFormatter));
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
    if(!params.get("messages.text").isEmpty() && !params.containsKey("messages.text")){
      textQueryFields.add("messages.text");
    }
    if(!params.get("messages.tags.intent").isEmpty() && !params.containsKey("messages.tags.intent")){
      textQueryFields.add("messages.tags.intent");
    }
    if(!params.get("messages.events.eventDetails").isEmpty() && !params.containsKey("messages.events.eventDetails")){
      textQueryFields.add("messages.events.eventDetails");
    }
    return textQueryFields;
  }

  private List<String> dateRangeFields(){
    return Arrays.asList("messages.from", "messages.to");
  }

  private List<String> paginationFields(){
    return Arrays.asList("page","size");
  }

  private List<JsonNode> handleResponse(SearchResponse searchResponse) {
    RestStatus status = searchResponse.status();
    TimeValue took = searchResponse.getTook();
    SearchHits hits = searchResponse.getHits();
    //long totalHits = hits.getTotalHits();
    SearchHit[] searchHits = hits.getHits();
    List<JsonNode> results = new ArrayList<>();
    for (SearchHit hit : searchHits) {
      String index = hit.getIndex();
      String id = hit.getId();
      float score = hit.getScore();
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      if(sourceAsMap.containsKey("sessionId")) {
        Session session = objectMapper.convertValue(sourceAsMap, Session.class);
        SessionAggregate sessionAggregate = getSessionAggregate(session);
        results.add(objectMapper.convertValue(sessionAggregate, JsonNode.class));
      }else{
        results.add(objectMapper.convertValue(sourceAsMap, JsonNode.class));
      }
    }
    System.out.println("Total Results Count "+results.size());
    System.out.println("Results "+results);
    return results;
  }

  private SessionAggregate getSessionAggregate(Session session) {
    SessionAggregate sessionAggregate = new SessionAggregate();
    sessionAggregate.setSessionId(session.getSessionId());
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
