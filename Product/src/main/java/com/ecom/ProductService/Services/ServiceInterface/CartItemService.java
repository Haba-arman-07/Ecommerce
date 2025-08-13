package com.ecom.ProductService.Services.ServiceInterface;


import com.ecom.CommonEntity.dtos.CartDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface CartItemService {

    ResponseModel addToCart(CartDto cartDto);

    ResponseModel getCartByUserId(Long userId);

    ResponseModel getAllCart();

    ResponseModel removeCartItem(Long cartId);

    ResponseModel clearCartByUserId(Long userId);

    ResponseModel updateQuantity(Long cartId, int quantity);

}