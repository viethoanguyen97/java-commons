package com.hovispace.javacommons.springkafka.producer;

import com.hovispace.javacommons.springkafka.entity.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class GreetingKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Greeting> _kafkaTemplate;

    @Autowired
    public GreetingKafkaProducer(KafkaTemplate<String, Greeting> kafkaTemplate) {
        _kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, Greeting greeting) {
        ListenableFuture<SendResult<String, Greeting>> future = _kafkaTemplate.send(topicName, greeting);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Greeting>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to send message {} due to : {}", greeting.getName(), throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Greeting> result) {
                LOGGER.info("Sent message {} with offset : {}", greeting.getName(), result.getRecordMetadata().offset());
            }
        });
    }
}
