package com.ecom.ProductService.Controllers;

import com.ecom.CommonEntity.dtos.CartDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/service/")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseModel addToCart(@RequestBody CartDto cartDto) {
        return cartService.addToCart(cartDto);
    }

    @GetMapping("{userId}")
    public ResponseModel getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @GetMapping
    public ResponseModel getAllCart(){
        return cartService.getAllCart();
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseModel removeCartItem(@PathVariable Long cartId) {
        return cartService.removeCartItem(cartId);
    }

    //user throw Remove
    @DeleteMapping("/user/{userId}")
    public ResponseModel clearCart(@PathVariable Long userId) {
        return cartService.clearCartByUserId(userId);
    }

    @PutMapping
    public ResponseModel updateQuantity(@RequestParam Long cartId, @RequestParam int quantity) {
        return cartService.updateQuantity(cartId, quantity);
    }

}
