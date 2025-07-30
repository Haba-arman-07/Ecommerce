package com.ecom.ProductService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.CartDto;
import com.ecom.CommonEntity.model.ResponseModel;


public interface CartService {
    ResponseModel addToCart(CartDto itemsDTO);

    ResponseModel getCartByUserId(Long userId);

    ResponseModel getAllCart();

    ResponseModel removeCartItem(Long cartId);

    ResponseModel clearCartByUserId(Long userId);

    ResponseModel updateQuantity(Long cartId, int quantity);

}
