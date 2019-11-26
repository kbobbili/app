package com.kalbob.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {

  /*@Bean
  public RestHighLevelClient esClient(){
    return new RestHighLevelClient(
        RestClient.builder(
            new HttpHost("localhost", 9200, "http")));
  }

  @Bean
  public ObjectMapper objectMapper(){
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
    return objectMapper;
  }*/

}
