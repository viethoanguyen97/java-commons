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
        // The send API returns a CompletableFuture object. If we want to block the sending thread and get the result about the sent message,
        // we can call the get API of the CompletableFuture object. The thread will wait for the result, but it will slow down the producer.
        // Kafka is a fast stream processing platform. So it's a better idea to handle the results asynchronously so that the subsequent messages do not wait for the result of the previous message.
        // We can do this through a callback:

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
