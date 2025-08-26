package com.ecom.ProductService.Controllers;


import com.ecom.CommonEntity.dtos.CartDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/")
    public ResponseModel addToCart(@RequestBody CartDto cartDto) {
        return cartItemService.addToCart(cartDto);
    }

    @GetMapping("/{userId}")
    public ResponseModel getCartByUserId(@PathVariable Long userId) {
        return cartItemService.getCartByUserId(userId);
    }

    @GetMapping("/")
    public ResponseModel getAllCart(){
        return cartItemService.getAllCart();
    }

    @DeleteMapping("/{cartId}")
    public ResponseModel removeCartItem(@PathVariable Long cartId) {
        return cartItemService.removeCartItem(cartId);
    }

    @DeleteMapping("/{userId}")
    public ResponseModel clearCart(@PathVariable Long userId) {
        return cartItemService.clearCartByUserId(userId);
    }

    @PutMapping("/")
    public ResponseModel updateQuantity(@RequestParam Long cartItemId, @RequestParam int quantity) {
        return cartItemService.updateQuantity(cartItemId, quantity);
    }

}
