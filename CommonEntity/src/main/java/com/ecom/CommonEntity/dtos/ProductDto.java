package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Product;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductDto implements Serializable {

    private Long productId;
    private String productName;
    private String description;
    private CategoryDto category;
    private Double price;
    private int qty;
    private String imageUrl;
    private int taxRate;
    private Status status;


    public static Product toEntity(ProductDto productDto){
        return Product.builder()
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .category(CategoryDto.toEntity(productDto.getCategory()))
                .price(productDto.getPrice())
                .qty(productDto.getQty())
                .imageUrl(productDto.getImageUrl())
                .taxRate(productDto.getTaxRate())
                .status(Status.ACTIVE)
                .build();
    }

    public static ProductDto toDto(Product product){
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .category(CategoryDto.toDto(product.getCategory()))
                .price(product.getPrice())
                .qty(product.getQty())
                .imageUrl(product.getImageUrl())
                .taxRate(product.getTaxRate())
                .status(product.getStatus())
                .build();
    }
}
