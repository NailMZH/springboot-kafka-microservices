package com.example.kafkaproducer.producer;


import com.example.kafkacommon.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductProducer.class);
    private final KafkaTemplate<String, ProductDto> kafkaTemplate;

    @Autowired
    public ProductProducer(KafkaTemplate<String, ProductDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProduct(ProductDto date, String topic) {

        LOGGER.info("New product sent -> {}", date.toString());

        Message<ProductDto> message = MessageBuilder
                .withPayload(date)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        kafkaTemplate.send(message);
    }

    public void requestProduct(ProductDto productDto){
        LOGGER.info("Request product by ID sent -> {}", productDto.toString());

        // Отправка запроса на получение продукта. Не ждём ответа, продолжаем работу
        kafkaTemplate.send("request-product-by-ID-topic", productDto);
    }
}

