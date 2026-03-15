package com.hovispace.javacommons.elasticsearch.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.hovispace.javacommons.elasticsearch.store.ElasticsearchPersonStore;
import com.hovispace.javacommons.elasticsearch.store.PersonStore;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }

    @Bean
    public ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        return objectMapper;
    }

    @Bean
    public PersonStore personStore(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        return new ElasticsearchPersonStore(restHighLevelClient, objectMapper);
    }
}
