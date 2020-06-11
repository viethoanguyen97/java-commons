package com.hovispace.javacommons.springkafka.config;

import com.google.common.collect.ImmutableMap;
import com.hovispace.javacommons.springkafka.entity.Greeting;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * For consuming messages, we need to configure a ConsumerFactory and a KafkaListenerContainerFactory.
 * Once these beans are available in the Spring bean factory, POJO based consumers can be configured using @KafkaListener annotation.
 *
 * @EnableKafka annotation is required on the configuration class to enable detection of @KafkaListener annotation on spring managed beans:
 */

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${kafka.bootstrapAddress")
    private String _bootstrapAddress;

    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        //We can replace ImmutableMap.of by Map.of or Map.ofEntries by setting module language to Java 9
        Map<String, Object> props = ImmutableMap.of(
            BOOTSTRAP_SERVERS_CONFIG, _bootstrapAddress,
            GROUP_ID_CONFIG, groupId,
            KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class,
            VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class
        );

        return new DefaultKafkaConsumerFactory<>(props);
    }

    public ConsumerFactory<String, Greeting> greetingConsumerFactory() {
        Map<String, Object> props = ImmutableMap.of(
            BOOTSTRAP_SERVERS_CONFIG, _bootstrapAddress,
            GROUP_ID_CONFIG, "greeting",
            KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class,
            VALUE_DESERIALIZER_CLASS_CONFIG, new JsonDeserializer(Greeting.class)
        );

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> fooKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("foo"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> barKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("bar"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> filterKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("filter"));
        factory.setRecordFilterStrategy(record -> record.value().contains("World"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> headersKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("header"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> partitionsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("partitions"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Greeting> greetingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Greeting> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(greetingConsumerFactory());

        return factory;
    }
}
