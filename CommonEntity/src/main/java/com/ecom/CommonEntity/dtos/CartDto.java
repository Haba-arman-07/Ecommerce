package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.Product;
import com.ecom.CommonEntity.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto {
    private Long cartId;

    private Long userId;

    private Long productId;

    private String name;

    private String imageUrl;

    private Double price;

    private int quantity;

    public static Cart toEntity(CartDto cartDto, Users users, Product product){
        return Cart.builder()
                .user(users)
                .product(product)
                .quantity(cartDto.getQuantity())
                .price(product.getPrice())
                .build();
    }

    public static CartDto toDto(Cart cart){
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .productId(cart.getProduct().getProductId())
                .name(cart.getProduct().getProductName())
                .imageUrl(cart.getProduct().getImageUrl())
                .price(cart.getPrice())
                .quantity(cart.getQuantity())
                .build();
    }
}

