package com.example.kafkaproducer.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Bean
    public NewTopic productTopic() {
        return TopicBuilder.name("request-product-by-ID-topic")
                .build();
    }

    @Bean
    public NewTopic productJsonTopic() {
        return TopicBuilder.name("product-save-DB-topic")
                .build();
    }

    @Bean
    public NewTopic productReceiveTopic() {
        return TopicBuilder.name("response-product-by-ID-topic")
                .build();
    }
}
