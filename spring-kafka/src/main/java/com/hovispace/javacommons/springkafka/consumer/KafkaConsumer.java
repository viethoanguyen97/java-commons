package com.hovispace.javacommons.springkafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_PARTITION_ID;

@Component
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    // Multiple listeners can be implemented for a topic, each with a different group Id. Furthermore, one consumer can listen for messages from various topics:
    // @KafkaListener(topics = "topic1, topic2", groupId = "foo")
    @KafkaListener(topics = "grapes", groupId = "foo")
    public void listen(String message) {
        LOGGER.info("Received message in group foo: {} ", message);
    }

    // Spring also supports retrieval of one or more message headers using the @Header annotation in the listener:
    @KafkaListener(topics = "headers")
    public void listenWithHeader(@Payload String message, @Header(RECEIVED_PARTITION_ID) int partition) {
        LOGGER.info("Received message: {}, from partition: {} ", message, partition);
    }

    // Consuming messages from a specific partition
    // Since the initialOffset has been sent to 0 in this listener, all the previously consumed messages from partitions 0 and three will be re-consumed every time this listener is initialized.
    // If setting the offset is not required, we can use the partitions property of @TopicPartition annotation to set only the partitions without the offset:
    // @KafkaListener(topicPartitions = @TopicPartition(topic = "topicName", partitions = { "0", "1" }))
    @KafkaListener(topicPartitions = @TopicPartition(topic = "partitioned", partitionOffsets = {
        @PartitionOffset(partition = "0", initialOffset = "0"),
        @PartitionOffset(partition = "3", initialOffset = "0")
    }), containerFactory = "partitionsKafkaListenerContainerFactory")
    public void listenToPartition(@Payload String message, @Header(RECEIVED_PARTITION_ID) int partition) {
        LOGGER.info("Received message: {}, from partition: {} ", message, partition);
    }

    // Listener with message filter, all the messages matching the filter will be discarded.
    @KafkaListener(topics = "filtered", containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithMessageFilter(String message) {
        LOGGER.info("Received message {} ", message);
    }
}
