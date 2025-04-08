package com.example.kafkaproducer.service;

import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaproducer.producer.ProductProducer;
import org.springframework.stereotype.Service;

@Service
public class ProductProducerService {
    private final ProductProducer productProducer;

    public ProductProducerService(ProductProducer productProducer) {
        this.productProducer = productProducer;
    }
    public void createProduct(ProductDto productDto) {
        productProducer.sendProduct(productDto, "product-save-DB-topic");
    }
    public void requestProduct(Long id){
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productProducer.requestProduct(productDto);
    }
}
