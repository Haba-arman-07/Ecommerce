package com.ecom.OrderService.services;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.CommonEntity.model.ResponseModel;

public interface OrderService {

    ResponseModel addOrder(Long userId,Long addressId);
    ResponseModel getOrderByOrderId(Long orderId);
    ResponseModel getAllOrders();
    ResponseModel getOrderByUserId(Long userId);
    ResponseModel cancelOrder(Long orderId);
    ResponseModel returnOrder(Long orderId);
    ResponseModel updateOrder(Long orderId, OrderStatus orderStatus);

}
