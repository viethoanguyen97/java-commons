package com.hovispace.javacommons.springkafka.producer;

import com.hovispace.javacommons.springkafka.entity.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class GreetingKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Greeting> _kafkaTemplate;

    @Autowired
    public GreetingKafkaProducer(KafkaTemplate<String, Greeting> kafkaTemplate) {
        _kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, Greeting greeting) {
        CompletableFuture<SendResult<String, Greeting>> future = _kafkaTemplate.send(topicName, greeting);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("Unable to send message {} due to : {}", greeting.getName(), ex.getMessage());
            } else {
                LOGGER.info("Sent message {} with offset : {}", greeting.getName(), result.getRecordMetadata().offset());
            }
        });
    }
}
