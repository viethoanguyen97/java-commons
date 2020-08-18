package com.hovispace.javacommons.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;

@SpringBootApplication(
    exclude = ElasticsearchRestClientAutoConfiguration.class
)
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}
