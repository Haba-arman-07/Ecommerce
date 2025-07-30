package com.ecom.OrderService.controller;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.dtos.OrdersDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.OrderService.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseModel addOrder(@RequestParam Long userId,@RequestParam Long addressId){
        return orderService.addOrder(userId,addressId);
    }

    @GetMapping("{orderId}")
    public ResponseModel getOrderByOrderId(@PathVariable Long orderId){
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping
    public ResponseModel getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("users/{userId}")
    public ResponseModel getOrderByUserId(@PathVariable Long userId){
        return orderService.getOrderByUserId(userId);
    }

    @PutMapping("{orderId}")
    public ResponseModel cancelOrder(@PathVariable Long orderId){
        return orderService.cancelOrder(orderId);
    }

    @PutMapping("return/{orderId}")
    public ResponseModel returnOrder(@PathVariable Long orderId){
        return orderService.returnOrder(orderId);
    }

    @PutMapping
    public ResponseModel updateOrder(@RequestParam Long orderId, @RequestParam OrderStatus orderStatus){
        return orderService.updateOrder(orderId, orderStatus);
    }

}
