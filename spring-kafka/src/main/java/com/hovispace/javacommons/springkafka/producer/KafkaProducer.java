package com.hovispace.javacommons.springkafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> _kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        _kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String message) {
        // The send API returns a CompletableFuture in Spring Kafka 3.x.
        // Handling results asynchronously via whenComplete avoids blocking the producer thread.
        CompletableFuture<SendResult<String, String>> future = _kafkaTemplate.send(topicName, message);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("Unable to send message {} due to : {}", message, ex.getMessage());
            } else {
                LOGGER.info("Sent message {} with offset : {}", message, result.getRecordMetadata().offset());
            }
        });
    }

    public void sendMessageToPartition(String topicName, String message, int partition) {
        _kafkaTemplate.send(topicName, partition, null, message);
    }

}
