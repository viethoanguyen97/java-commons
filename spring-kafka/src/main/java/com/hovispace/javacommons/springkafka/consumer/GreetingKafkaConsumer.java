package com.hovispace.javacommons.springkafka.consumer;

import com.hovispace.javacommons.springkafka.entity.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class GreetingKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "greeting", containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        LOGGER.info("Received message: {}, {}", greeting.getName(), greeting.getMessage());
    }
}
