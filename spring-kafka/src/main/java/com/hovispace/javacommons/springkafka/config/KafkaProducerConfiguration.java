package com.hovispace.javacommons.springkafka.config;

import com.google.common.collect.ImmutableMap;
import com.hovispace.javacommons.springkafka.entity.Greeting;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * To produce messages into Apache Kafka, we need to configure a ProducerFactory which sets the strategy for creating Kafka Producer instances.
 * Producer instances are thread-safe and hence using a single instance throughout an application context will give higher performance.
 * Consequently, KakfaTemplate instances are also thread-safe and use of one instance is recommended.
 */
@Configuration
public class KafkaProducerConfiguration {

    @Value("${kafka.bootstrapAddress}")
    private String _bootstrapAddress;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = ImmutableMap.of(
            BOOTSTRAP_SERVERS_CONFIG, _bootstrapAddress,
            KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * KafkaTemplate which wraps a Producer instance and provides convenience methods for sending messages to Kafka topics.
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public ProducerFactory<String, Greeting> greetingProducerFactory() {
        Map<String, Object> configProps = ImmutableMap.of(
            BOOTSTRAP_SERVERS_CONFIG, _bootstrapAddress,
            KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Greeting> greetingKafkaTemplate() {
        return new KafkaTemplate<>(greetingProducerFactory());
    }

}
