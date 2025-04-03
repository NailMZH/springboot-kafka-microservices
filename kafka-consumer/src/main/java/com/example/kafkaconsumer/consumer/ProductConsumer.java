package com.example.kafkaconsumer.consumer;


import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaconsumer.model.Product;
import com.example.kafkaconsumer.repository.ProductRepository;
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

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductDto> kafkaTemplate;

    @Autowired
    public ProductConsumer(ProductRepository productRepository, KafkaTemplate<String, ProductDto> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "product-save-DB-topic", groupId = "my-group")
    public void consumeSaving(ProductDto productDto) { //Spring Kafka provided JsonDeserializer will convert ProductDto JSON object into Java ProductDto object

        try {
            LOGGER.info("consumeSaving method called");
            LOGGER.info(String.format("Message received -> %s", productDto.toString()));

            // Преобразование DTO в сущность
            Product product = new Product();
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());

            // Сохранение продукта в базе данных
            productRepository.save(product);

            LOGGER.info("Product saved to database: " + product.toString());
        }catch(Exception e) {
            LOGGER.error("Error while consuming message: ", e);
        }
    }

    @KafkaListener(topics = "request-product-by-ID-topic", groupId = "my-group")
    public void consumeRequest(ProductDto requestDto) {
        try {
            LOGGER.info("Received request for product with ID: " + requestDto.getId());
            Optional<Product> existingProduct = productRepository.findById(requestDto.getId());

            if (existingProduct.isPresent()) {
                // Отправка найденного продукта
                Product product = existingProduct.get();
                ProductDto responseDto = new ProductDto(
                        product.getId(), product.getName(), product.getDescription());
                kafkaTemplate.send("response-product-by-ID-topic", responseDto);
                LOGGER.info("Product found and sent in response: " + responseDto.toString());
            } else {
                LOGGER.warn("Product with ID " + requestDto.getId() + " not found ");
            }
        }catch (Exception e) {
            LOGGER.error("Error processing request for product with ID: {}", requestDto.getId(), e);
        }
    }
}
