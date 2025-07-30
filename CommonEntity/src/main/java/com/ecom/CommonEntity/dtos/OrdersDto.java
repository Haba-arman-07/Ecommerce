package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.entities.Orders;
import com.ecom.CommonEntity.entities.Product;
import com.ecom.CommonEntity.entities.Users;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdersDto {
    private Long orderId;
    private Long user;
    private Long product;
    private double totalAmount;
    private int qty;
    private OrderStatus orderStatus;

    public static Orders toEntity(OrdersDto ordersDto, Users users, Product product){
        return Orders.builder()
                .user(users)
                .product(product)
                .totalAmount(ordersDto.getTotalAmount())
                .qty(ordersDto.getQty())
                .orderStatus(ordersDto.getOrderStatus())
                .build();
    }

    public static OrdersDto toDto(Orders orders){
        return OrdersDto.builder()
                .orderId(orders.getOrderId())
                .user(orders.getUser().getUserId())
                .product(orders.getProduct().getProductId())
                .totalAmount(orders.getTotalAmount())
                .qty(orders.getQty())
                .orderStatus(orders.getOrderStatus())
                .build();
    }

}