package com.ecom.OrderService.controller;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.OrderService.services.ServiceInterface.OrderService;
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

    @GetMapping
    public ResponseModel getAllOrder(){
        return orderService.getAllOrder();
    }

    @GetMapping("user/{userId}")
    public ResponseModel getUserOrder(@PathVariable Long userId){
        return orderService.getUserOrder(userId);
    }

    @GetMapping("{orderItemId}")
    public ResponseModel getOrderItem(@PathVariable Long orderItemId){
        return orderService.getOrderItem(orderItemId);
    }

    @PutMapping
    public ResponseModel updateOrder(@RequestParam Long orderId, @RequestParam OrderStatus orderStatus){
        return orderService.updateOrder(orderId,orderStatus);
    }

    @PutMapping("cancel/{orderItemId}")
    public ResponseModel cancelOrder(@PathVariable Long orderItemId){
        return orderService.cancelOrderItem(orderItemId);
    }

    @PutMapping("return/{orderItemId}")
    public ResponseModel returnOrder(@PathVariable Long orderItemId){
        return orderService.returnOrder(orderItemId);
    }
}