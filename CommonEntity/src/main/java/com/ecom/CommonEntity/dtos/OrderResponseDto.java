package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.OrderItem;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;

    private Long userId;

    private String fullName;

    private String mobile;

    private String email;

    private Status status;

    private Long addressId;

    private Long zipCode;

    private String countryName;

    private String stateName;

    private String cityName;

    private Long orderItemId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Double productPrice;

    private Double totalAmount;

    private int quantity;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    private List<OrderItem> orderItems;

    public static OrderResponseDto responseDto(OrderItem orderItem){
        return OrderResponseDto.builder()
                .orderId(orderItem.getOrders().getOrderId())
                .userId(orderItem.getOrders().getUser().getUserId())
                .fullName(orderItem.getOrders().getUser().getFirstName() + " " + orderItem.getOrders().getUser().getLastName())
                .mobile(orderItem.getOrders().getUser().getMobile())
                .email(orderItem.getOrders().getUser().getEmail())
                .addressId(orderItem.getOrders().getAddress().getAddressId())
                .zipCode(orderItem.getOrders().getAddress().getZipCode())
                .countryName(orderItem.getOrders().getAddress().getCountry().getCountryName())
                .stateName(orderItem.getOrders().getAddress().getState().getStateName())
                .cityName(orderItem.getOrders().getAddress().getCity().getCityName())
                .totalAmount(orderItem.getOrders().getTotalAmount())
//                .orderItems(orderItem.stream()
//                        .map(item -> OrderItem.builder()
//                                .orderItemId(item.getOrderItemId())
//                                .product(item.getProduct())
//                                .quantity(item.getQuantity())
//                                .price(item.getPrice())
//                                .build())
//                        .toList())
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .productName(orderItem.getProduct().getProductName())
                .imageUrl(orderItem.getProduct().getImageUrl())
                .quantity(orderItem.getQuantity())
                .productPrice(orderItem.getPrice())
                .orderStatus(orderItem.getOrderStatus())
                .createdAt(orderItem.getOrders().getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .status(orderItem.getOrders().getUser().getStatus())
                .build();
    }
}
