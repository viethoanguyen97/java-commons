package com.hovispace.javacommons.springkafka;

import com.hovispace.javacommons.springkafka.consumer.GreetingKafkaConsumer;
import com.hovispace.javacommons.springkafka.consumer.KafkaConsumer;
import com.hovispace.javacommons.springkafka.entity.Greeting;
import com.hovispace.javacommons.springkafka.producer.GreetingKafkaProducer;
import com.hovispace.javacommons.springkafka.producer.KafkaProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        KafkaProducer kafkaProducer = applicationContext.getBean(KafkaProducer.class);
        KafkaConsumer kafkaConsumer = applicationContext.getBean(KafkaConsumer.class);
        GreetingKafkaConsumer greetingKafkaConsumer = applicationContext.getBean(GreetingKafkaConsumer.class);
        GreetingKafkaProducer greetingKafkaProducer = applicationContext.getBean(GreetingKafkaProducer.class);

        // sending Hello World message to topic 'grapes'
        kafkaProducer.sendMessage("grapes", "Hello, World!");

        // sending message to a topic with 5 partition, each message to a different partition
        // but as per listener configuration in KafkaConsumer, only messages from partition 0 & 3 will be consumed.
        for (int i = 0; i < 5; i++) {
            kafkaProducer.sendMessageToPartition("partitioned", "Hello to partitioned topic!", i);
        }

        // sending message to filtered topic
        // message with char sequence 'World' will be discarded
        kafkaProducer.sendMessage("filtered", "Hello Grapes");
        kafkaProducer.sendMessage("filtered", "Hello World");

        // sending message to greeting topic
        greetingKafkaProducer.sendMessage("greeting", new Greeting("Greetings", "World!"));

        applicationContext.close();
    }
}
