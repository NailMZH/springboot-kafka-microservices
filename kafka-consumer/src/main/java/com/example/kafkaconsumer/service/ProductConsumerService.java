package com.example.kafkaconsumer.service;

import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaconsumer.mapper.ProductMapper;
import com.example.kafkaconsumer.model.Product;
import com.example.kafkaconsumer.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductConsumerService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, ProductDto> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductConsumerService.class);

    @Autowired
    public ProductConsumerService(ProductRepository productRepository, ProductMapper productMapper,
                                  KafkaTemplate<String, ProductDto> kafkaTemplate) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void saveProduct(ProductDto productDto) {
        LOGGER.info("Before mapping: {}", productDto);
        Product product = productMapper.toEntity(productDto);
        LOGGER.info("After mapping. Product save: {}", product);
        productRepository.save(product);
    }

    public Optional<ProductDto> handleProductRequest(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .map(productDto -> {
                    sendResponse(productDto);
                    return productDto;
                });
    }

    public void sendResponse(ProductDto productDto) {
        kafkaTemplate.send("response-product-by-ID-topic", productDto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOGGER.error("Failed to send response: {}", productDto, ex);
                    } else {
                        LOGGER.info("Response sent successfully: {}", productDto);
                    }
                });
    }
}
