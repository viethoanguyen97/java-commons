package com.hovispace.javacommons.springkafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import static java.util.Collections.singletonMap;
import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * Previously we used to run command line tools to create topics in Kafka such as:
 * $ bin/kafka-topics.sh --create \
 *   --zookeeper localhost:2181 \
 *   --replication-factor 1 --partitions 1 \
 *   --topic mytopic
 * But with the introduction of AdminClient in Kafka, we can now create topics programmatically.
 *
 * We need to add the KafkaAdmin Spring bean, which will automatically add topics for all beans of type NewTopic:
 */

@Configuration
public class KafkaTopicConfiguration {

    @Value("${kafka.bootstrapAddress}")
    private String _bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(singletonMap(BOOTSTRAP_SERVERS_CONFIG, _bootstrapAddress));
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("grapes", 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic("partitioned", 6, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic("filtered", 1, (short) 1);
    }

    @Bean
    public NewTopic topic4() {
        return new NewTopic("greeting", 1, (short) 1);
    }
}
