package com.example.kafkaconsumer.mapper;

import com.example.kafkacommon.dto.ProductDto;
import com.example.kafkaconsumer.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) // Указываем "spring" для интеграции с Spring
public interface ProductMapper {

    // Метод для преобразования DTO в сущность
    Product toEntity(ProductDto dto);

    // Метод для преобразования сущности в DTO
    ProductDto toDto(Product entity);
}


