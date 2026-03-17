package com.hovispace.javacommons.elasticsearch.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.hovispace.javacommons.elasticsearch.store.ElasticsearchPersonStore;
import com.hovispace.javacommons.elasticsearch.store.PersonStore;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Bean
    public Rest5Client rest5Client() {
        return Rest5Client.builder(new HttpHost("localhost", 9200)).build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(Rest5Client rest5Client) {
        Rest5ClientTransport transport = new Rest5ClientTransport(rest5Client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean
    public ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        return objectMapper;
    }

    @Bean
    public PersonStore personStore(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchPersonStore(elasticsearchClient);
    }
}
