package com.example.kafkacommon.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class ProductDto implements Serializable {

    private Long id;

    @NotNull(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Description is mandatory")
    private String description;
}