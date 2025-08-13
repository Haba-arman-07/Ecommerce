package com.ecom.CommonEntity.dtos;


import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.Product;
import com.ecom.CommonEntity.entities.CartItem;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemsDto {

    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private Double price;
    private int quantity;

    public static CartItem toEntity(CartItemsDto dto, Cart cart, Product product) {
        return CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(dto.getQuantity())
                .price(product.getPrice()) // Optionally override this with dto.getPrice()
                .build();
    }

    public static CartItemsDto toDto(CartItem cartItem) {
        return CartItemsDto.builder()
                .cartItemId(cartItem.getCartItemsId())
                .cartId(cartItem.getCart().getCartId())
                .productId(cartItem.getProduct().getProductId())
                .productName(cartItem.getProduct().getProductName())
                .imageUrl(cartItem.getProduct().getImageUrl())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
