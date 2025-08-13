package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.Users;
import lombok.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto {

    private Long cartId;

    public Double totalAmount;

    private Long userId;

    private List<CartItemsDto> cartItems;

    public static Cart toEntity(CartDto cartDto, Users user){
        return Cart.builder()
                .user(user)
                .totalAmount(cartDto.getTotalAmount())
                .build();
    }

    public static CartDto toDto(Cart cart) {
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .totalAmount(cart.getTotalAmount())
                .cartItems(
                        cart.getCartItems() != null ?
                                cart.getCartItems().stream()
                                        .map(CartItemsDto::toDto)
                                        .toList() : null
                )
                .build();
    }

}