package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.entities.Address;
import com.ecom.CommonEntity.entities.Orders;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdersFeedDto {

    private Long orderId;
    private Long userId;
    private String userName;
    private String email;
    private Double totalAmount;
    private Long addressId;
    private String countryName;
    private String stateName;
    private String cityName;
    private Long zipCode;
    private String location;
    private Long productId;
    private String productName;
    private Double price;
    private String imageUrl;
    private OrderStatus orderStatus;
    private int qty;
//    private List<ProductDto> productList;

    public static OrdersFeedDto response(Orders orders){

//        Address address = orders.getAddress();

        return OrdersFeedDto.builder()
                .orderId(orders.getOrderId())
                .userId(orders.getUser().getUserId())
                .userName(orders.getUser().getFirstName() + " " + orders.getUser().getLastName())
                .email(orders.getUser().getEmail())
                .totalAmount(orders.getTotalAmount())
                .addressId(orders.getAddress().getAddressId())
                .countryName(orders.getAddress().getCountry().getCountryName())
                .stateName(orders.getAddress().getState().getStateName())
                .cityName(orders.getAddress().getCity().getCityName())
                .zipCode(orders.getAddress().getZipCode())
                .location(orders.getAddress().getLocation())
                .productId(orders.getProduct().getProductId())
                .productName(orders.getProduct().getProductName())
                .price(orders.getProduct().getPrice())
                .qty(orders.getQty())
                .imageUrl(orders.getProduct().getImageUrl())
                .orderStatus(orders.getOrderStatus())
                .build();
    }

}