package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.entities.CartItem;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponseDto {

    private Long cartItemId;

    private Long cartId;

    private Long userId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Double price;

    private int quantity;

    public Double totalAmount;

    public static CartResponseDto responseDto(CartItem cartItem){
        return CartResponseDto.builder()
                .cartItemId(cartItem.getCartItemsId())
                .cartId(cartItem.getCart().getCartId())
                .totalAmount(cartItem.getCart().getTotalAmount())
                .userId(cartItem.getCart().getUser().getUserId())
                .productId(cartItem.getProduct().getProductId())
                .productName(cartItem.getProduct().getProductName())
                .imageUrl(cartItem.getProduct().getImageUrl())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
