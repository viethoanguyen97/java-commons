package com.hovispace.javacommons.springkafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> _kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        _kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String message) {
        // The send API returns a ListenableFuture object. If we want to block the sending thread and get the result about the sent message,
        // we can call the get API of the ListenableFuture object. The thread will wait for the result, but it will slow down the producer.
        // Kafka is a fast stream processing platform. So it's a better idea to handle the results asynchronously so that the subsequent messages do not wait for the result of the previous message.
        // We can do this through a callback:

        ListenableFuture<SendResult<String, String>> future = _kafkaTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to send message {} due to : {}", message, throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOGGER.info("Sent message {} with offset : {}", message, result.getRecordMetadata().offset());
            }
        });
    }

    public void sendMessageToPartition(String topicName, String message, int partition) {
        _kafkaTemplate.send(topicName, partition, null, message);
    }

}
