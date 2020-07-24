package com.hovispace.javacommons.elasticsearch.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    //Before we jump straight to how to use the main Java API features, we need to initiate the RestHighLevelClient:
    @Bean
    public RestHighLevelClient restHighLevelClient() {

        return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9202, "http"))
        );
    }

}
