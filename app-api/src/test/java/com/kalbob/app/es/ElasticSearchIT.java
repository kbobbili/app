package com.kalbob.app.es;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private RestHighLevelClient client = new RestHighLevelClient(
      RestClient.builder(
          new HttpHost("localhost", 9200, "http")));
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void test() throws JsonProcessingException {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    System.out.println(now);
    System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS").format(now));
    System.out.println(objectMapper.writeValueAsString(getMessage()));
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

  private HistMessage getMessage(){
    Map<String, Object> msgPayload = new HashMap<>();
    msgPayload.put("text", "hello! how are you");
    msgPayload.put("user", new User().setUserId("USER-1"));
    Map<String, Object> tags = new HashMap<>();
    tags.put("intent", "welcome");
    tags.put("lang", "en-US");
    tags.put("confidence", 1);
    HistMessage message = new HistMessage()
        .setTenantId("ABC")
        .setApplication("IQ")
        .setChannel("twilio")
        .setChannelUserId("13343329276")
        .setSessionId("SESSION-1")
        .setMessage(msgPayload)
        .setMessageDirection("INBOUND")
        .setSentAt(ZonedDateTime.now(ZoneId.of("UTC")))
        .setApplicationMeta(new ApplicationMeta().setVersion("v0"))
        .setTags(tags);
    return message;
  }

  @Test
  public void index() throws IOException {
    HistMessage message = getMessage();
    String document = objectMapper.writeValueAsString(message);
    IndexRequest request = new IndexRequest("messages-01","_doc")
        .source(document, XContentType.JSON);
        //.version(1) //.opType(DocWriteRequest.OpType.CREATE);
    IndexResponse response = null;
    try {
      response = client.index(request, RequestOptions.DEFAULT);
    } catch(ElasticsearchException e) {
      if (e.status() == RestStatus.CONFLICT) {
        logger.error("Index {} & Type {} already has a Document with id {}", request.index(), request.type(), request.id());
      }
    }
    assertEquals(response.getResult(), DocWriteResponse.Result.CREATED);
  }



}
