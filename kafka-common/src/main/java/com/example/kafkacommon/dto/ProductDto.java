package com.example.kafkacommon.dto;


import java.io.Serializable;

public class ProductDto implements Serializable {
    private Long id;
    private String name;
    private String description;

    public ProductDto() {
    }

    public ProductDto(Long id) {
        this.id = id;
    }

    public ProductDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

// Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description=" + description +
                '}';
    }
}