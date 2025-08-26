package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductFeedDto implements Serializable {
    private long productId;
    private String productName;
    private String imageUrl;
    private Double price;
    private long categoryId;
    private String categoryName;
    private Status status;

    public ProductFeedDto(long productId, String productName, String imageUrl
            ,Double price, long categoryId, String categoryName, Status status) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
    }
}