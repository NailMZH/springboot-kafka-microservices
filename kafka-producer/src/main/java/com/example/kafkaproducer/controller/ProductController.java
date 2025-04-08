package com.example.kafkaproducer.controller;

import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaproducer.service.ProductProducerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductProducerService producerService;
    private final View error;

    @Autowired
    public ProductController(ProductProducerService producerService, View error) {
        this.producerService = producerService;
        this.error = error;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDto productDto) {
        // Отправка сообщения в Kafka
        producerService.createProduct(productDto);
        return ResponseEntity.ok("New product sent to Kafka");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex){
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body("Validation failed: " + errorMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> sendProductToKafka(@PathVariable Long id) {

        producerService.requestProduct(id);
        return ResponseEntity.ok("Product request sent to Kafka: " + id);
    }
}
