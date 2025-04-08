package com.example.kafkaconsumer.consumer;


import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaconsumer.service.ProductConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProductConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    private final KafkaTemplate<String, ProductDto> kafkaTemplate;
    private final ProductConsumerService productConsumerService;

    @Autowired
    public ProductConsumer(KafkaTemplate<String, ProductDto> kafkaTemplate, ProductConsumerService productConsumerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.productConsumerService = productConsumerService;
    }

    @KafkaListener(topics = "product-save-DB-topic", groupId = "my-group")
    public void consumeSaving(ProductDto productDto) {
        try {
            LOGGER.info("Received product for saving: {}", productDto);
            productConsumerService.saveProduct(productDto);
        } catch (Exception e) {
            LOGGER.error("Error processing product save: {}", productDto, e);
        }
    }

    @KafkaListener(topics = "request-product-by-ID-topic", groupId = "my-group")
    public void consumeRequest(ProductDto requestDto) {
        try {
            LOGGER.info("Received product request: {}", requestDto);
            Optional<ProductDto> productOpt = productConsumerService.handleProductRequest(requestDto.getId());

            if (productOpt.isEmpty()) {
                LOGGER.error("Product tot found ID: {}", requestDto);
            }

        } catch (Exception e) {
            LOGGER.error("Error processing product request: {}", requestDto.getId());
        }
    }
}
