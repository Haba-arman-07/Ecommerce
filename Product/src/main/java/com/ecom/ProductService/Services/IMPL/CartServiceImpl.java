package com.ecom.ProductService.Services.IMPL;

import com.ecom.CommonEntity.dtos.CartDto;
import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.Product;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.dao.MasterDao;
import com.ecom.ProductService.Services.ServiceInterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private MasterDao masterDao;

    // addToCart
    @Override
    public ResponseModel addToCart(CartDto cartDto) {
        try {
            Optional<Users> user = masterDao.getUserRepo().findById(cartDto.getUserId());
            Optional<Product> product = masterDao.getProductRepo().findById(cartDto.getProductId());

            if (user.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "User not found"
                );
            }

            if (product.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "Product not found"
                );
            }

            Cart cart = CartDto.toEntity(cartDto, user.get(), product.get());
            Cart saved = masterDao.getCartRepo().save(cart);

            return new ResponseModel(
                    HttpStatus.OK,
                    CartDto.toDto(saved),
                    "Item added to cart"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Internal Server Error"
            );
        }
    }

    //Get Cart By userId
    @Override
    public ResponseModel getCartByUserId(Long userId) {
        try {
            Optional<Users> user = masterDao.getUserRepo().findById(userId);

            if (user.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "User not found"
                );
            }else {

                List<Cart> cartList = masterDao.getCartRepo().findByUserId(userId);

                if (cartList.isEmpty()){
                    return new ResponseModel(
                            HttpStatus.NOT_FOUND,
                            null,
                            "User Not Found"
                    );
                }

                List<CartDto> cart = cartList.stream()
                        .map(c -> CartDto.toDto(c))
                        .toList();

                return new ResponseModel(
                        HttpStatus.OK,
                        cart,
                        "Cart items retrieved"
                );
            }
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Error: " + e.getMessage());
        }
    }

    //Get All Cart Items
    public ResponseModel getAllCart(){
        List<Cart> cart = masterDao.getCartRepo().findAll();

        if (cart.isEmpty()){
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Cart Is Empty"
            );
        }

        List<CartDto> cartDtos = cart.stream()
                .map(c -> CartDto.toDto(c))
                .toList();

        return new ResponseModel(
                HttpStatus.OK,
                cartDtos,
                "SUCCESS"
        );
    }

    //Remove CartItems From Cart
    @Override
    public ResponseModel removeCartItem(Long cartId) {
        try {
            boolean exists = masterDao.getCartRepo().existsById(cartId);
            if (!exists) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "Cart item not found"
                );
            }

            masterDao.getCartRepo().deleteById(cartId);
            return new ResponseModel(
                    HttpStatus.OK,
                    null,
                    "Item removed from cart"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Error: " + e.getMessage()
            );
        }
    }

    //Remove All Cart Items
    @Override
    public ResponseModel clearCartByUserId(Long userId) {
        try {
            Optional<Users> user = masterDao.getUserRepo().findById(userId);
            if (user.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "User not found"
                );
            }

            List<Cart> userCart = masterDao.getCartRepo().findByUserId(userId);

            if (userCart.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NO_CONTENT,
                        null,
                        "User Id Not Found Of Cart"
                );
            }

            masterDao.getCartRepo().deleteAll(userCart);

            return new ResponseModel(
                    HttpStatus.OK,
                    null,
                    "user remove successfully"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "ERROR "
            );
        }
    }

    //Update Quantity
    @Override
    public ResponseModel updateQuantity(Long cartId, int quantity) {
        try {
            Optional<Cart> existCartItems = masterDao.getCartRepo().findById(cartId);
            if (existCartItems.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "Cart item not found"
                );
            } else {

                Cart cart = existCartItems.get();

                double productPrice = cart.getProduct().getPrice();

                cart.setQuantity(quantity);
                cart.setPrice(productPrice * quantity);
                cart.setUpdatedAt(LocalDateTime.now());

                Cart saveCart = masterDao.getCartRepo().save(cart);

                return new ResponseModel(
                        HttpStatus.OK,
                        CartDto.toDto(saveCart),
                        "Quantity and price updated successfully"
                );
            }
        }catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Error: " + e.getMessage()
            );
        }
    }

}


