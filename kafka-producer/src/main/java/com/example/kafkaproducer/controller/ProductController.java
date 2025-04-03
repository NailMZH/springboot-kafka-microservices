package com.example.kafkaproducer.controller;

import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaproducer.producer.ProductProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductProducer productProducer;

    @Autowired
    public ProductController(ProductProducer productProducer) {
        this.productProducer = productProducer;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductDto productDto) {
        // Отправка сообщения в Kafka
        productProducer.sendProduct(productDto, "product-save-DB-topic");
        return ResponseEntity.ok("New product sent to Kafka");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> sendProductToKafka(@PathVariable Long id) {
        ProductDto productDto = new ProductDto(id); // Создайте DTO с ID продукт
        productProducer.requestProduct(id); // Отправка запроса
        return ResponseEntity.ok("Product request sent to Kafka: " + id);
    }
}
