package com.kalbob.code.config.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mapping.MappingException;


@Data
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(value = "com.kalbob.app.es", ignoreInvalidFields = true)
@EnableElasticsearchRepositories(basePackages = {"com.kalbob.code.es"}, considerNestedRepositories = true)
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {

  private String url;

  @Override
  public RestHighLevelClient elasticsearchClient() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(url)
        .withConnectTimeout(Duration.ofSeconds(5))
        .withSocketTimeout(Duration.ofSeconds(3))
        .build();
    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchRestTemplate elasticsearchRestTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient(), entityMapper());
  }

  @Bean
  @Override
  public EntityMapper entityMapper() {
    return new CustomEntityMapper(objectMapper());
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    objectMapper.registerModule(new CustomGeoModule());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
    return objectMapper;
  }

}

class CustomEntityMapper implements EntityMapper {

  private ObjectMapper objectMapper;

  CustomEntityMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String mapToString(Object object) throws IOException {
    return this.objectMapper.writeValueAsString(object);
  }

  @Override
  public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
    return this.objectMapper.readValue(source, clazz);
  }

  @Override
  public Map<String, Object> mapObject(Object source) {

    try {
      return objectMapper.readValue(mapToString(source), HashMap.class);
    } catch (IOException e) {
      throw new MappingException(e.getMessage(), e);
    }
  }

  @Override
  public <T> T readObject (Map<String, Object> source, Class<T> targetType) {

    try {
      return mapToObject(mapToString(source), targetType);
    } catch (IOException e) {
      throw new MappingException(e.getMessage(), e);
    }
  }
}
