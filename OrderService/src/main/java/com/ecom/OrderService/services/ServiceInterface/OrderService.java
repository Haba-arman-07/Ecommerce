package com.ecom.OrderService.services.ServiceInterface;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.model.ResponseModel;

public interface OrderService {
    ResponseModel addOrder(Long userId,Long addressId);
    ResponseModel getAllOrder();
    ResponseModel getUserOrder(Long userId);
    ResponseModel getOrderItem(Long orderItemId);
    ResponseModel updateOrder(Long orderId, OrderStatus orderStatus);
    ResponseModel cancelOrderItem(Long orderItemId);
    ResponseModel returnOrder(Long orderItemId);
}
