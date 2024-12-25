package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID productId;
    @NotBlank
    private String productName;
    @NotBlank
    private String description;
    private String imageSrc;
    @NotNull
    private QuantityState quantityState;
    @NotNull
    private ProductState productState;
    private ProductCategory productCategory;
    @PositiveOrZero
    private Long rating;
    @PositiveOrZero
    private Double price;
}


