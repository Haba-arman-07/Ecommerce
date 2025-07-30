package com.ecom.OrderService.services.impl;


import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.OrdersDto;
import com.ecom.CommonEntity.dtos.OrdersFeedDto;
import com.ecom.CommonEntity.entities.*;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.OrderService.dao.MasterDao;
import com.ecom.OrderService.dao.OrderDao;
import com.ecom.OrderService.services.OrderService;
import com.ecom.OrderService.utils.RabbitMQProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

import static com.ecom.OrderService.config.RabbitMQConfig.ORDER_MAIL_EXCHANGE;
import static com.ecom.OrderService.config.RabbitMQConfig.ROUTING_KEY_ORDER_MAIL;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MasterDao masterDao;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public ResponseModel addOrder(Long userId,Long addressId) {
        try {
            List<Cart> cartList = masterDao.getCartRepo().findByUserId(userId);

            Users users = masterDao.getUserRepo().findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

            Address address = masterDao.getAddressRepo().findByUser_UserIdAndAddressId(userId,addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address Not Found"));

            List<Orders> orders = new ArrayList<>();

            for (Cart cartItem : cartList) {
                Product product = cartItem.getProduct();
                if (product == null || product.getStatus() == Status.INACTIVE) {
                    throw new IllegalArgumentException("Product Not Exist | Product Is Deleted");
                }

                Orders order = new Orders();
                double totalAmount = product.getPrice() * cartItem.getQuantity();
                order.setUser(cartItem.getUser());
                order.setProduct(cartItem.getProduct());
                order.setQty(cartItem.getQuantity());
                order.setTotalAmount(totalAmount);
                order.setOrderStatus(OrderStatus.PENDING);
                order.setAddress(address);
                orders.add(order);
            }
                List<Orders> savedOrder = orderDao.saveAllOrders(orders);

                List<OrdersFeedDto> result = savedOrder.stream()
                        .map(OrdersFeedDto::response)
                        .toList();

                try {
                    for (OrdersFeedDto dto : result) {
                        Map<String, String> map = Map.of(
                                "UserName", dto.getUserName(),
                                "email", dto.getEmail(),
                                "orderId", String.valueOf(dto.getOrderId()),
                                "productName", dto.getProductName(),
                                "Quantity", String.valueOf(dto.getQty()),
                                "Price", String.valueOf(dto.getTotalAmount()),
                                "Message", "Your Order Is " + dto.getOrderStatus().toString()
                        );

//                        ObjectMapper mapper = new ObjectMapper();
                        rabbitMQProducer.sendMessage(mapper.writeValueAsString(map),
                                ORDER_MAIL_EXCHANGE, ROUTING_KEY_ORDER_MAIL);
                    }
                } catch (JsonProcessingException e){
                    e.getMessage();
                }

            return new ResponseModel(
                        HttpStatus.OK,
                        result,
                        "Order added successfully"
                );


        } catch (IllegalArgumentException e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel getOrderByOrderId(Long orderId){
        try {

            Optional<Orders> existOrder = orderDao.findOrderById(orderId);

            if (existOrder.isPresent()) {
                Orders order = existOrder.get();

                return new ResponseModel(
                        HttpStatus.OK,
                        OrdersDto.toDto(order),
                        "SUCCESS"
                );
            }

            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Order Doesn't Present"
            );
        }catch (Exception e){
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed To Fetch Order"
            );
        }

    }

    @Override
    public ResponseModel getAllOrders() {
        try {

            List<OrdersFeedDto> Orders = orderDao.findAllOrders().stream()
                    .map(OrdersFeedDto::response)
                    .toList();


            return new ResponseModel(
                    HttpStatus.OK,
                    Orders,
                    "Orders Get Successfully"
            );
        } catch (Exception e){
            return new ResponseModel(
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                "Failed TO Fetch Orders"
            );
        }
    }

    @Override
    public ResponseModel getOrderByUserId(Long userId) {
        try {
            List<OrdersFeedDto> existUser = orderDao.findOrderByUserId(userId, Status.ACTIVE).stream()
                    .map(OrdersFeedDto::response)
                    .toList();

            if (existUser.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "User Not Found"
                );

            }
            return new ResponseModel(
                    HttpStatus.OK,
                    existUser,
                    "SUCCESS"
            );

        } catch (Exception e){
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed To fetch Order By UserId"
            );
        }
    }

    @Override
    public ResponseModel cancelOrder(Long orderId){
        try {
            Orders existOrder = orderDao.findOrderById(orderId)
                    .orElseThrow(() -> new NoSuchElementException("Order Not Found"));

                if (existOrder.getOrderStatus() == OrderStatus.PENDING ||
                        existOrder.getOrderStatus() == OrderStatus.PROCESSING) {

                        existOrder.setOrderStatus(OrderStatus.CANCELLED);
                        Orders orders = orderDao.saveOrder(existOrder);
                        return new ResponseModel(
                                HttpStatus.OK,
                                OrdersFeedDto.response(orders),
                                "Order Cancel Successfully"
                        );
                }

                return new ResponseModel(
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "Your Order Already " + existOrder.getOrderStatus()
                );

            } catch (NoSuchElementException e){
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        e.getMessage()
                );
        } catch (Exception e){
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed TO Fetch Order"
            );
        }
    }

    @Override
    public ResponseModel returnOrder(Long orderId) {
        try {
            Orders existOrder = orderDao.findOrderById(orderId)
                    .orElseThrow(() -> new NoSuchElementException("Order Not Found"));

            if (existOrder.getOrderStatus() == OrderStatus.DELIVERED &&
                    existOrder.getUpdatedAt().plusDays(7).isAfter(LocalDateTime.now())) {

                existOrder.setOrderStatus(OrderStatus.RETURN);
                Orders savedOrder = orderDao.saveOrder(existOrder);

                return new ResponseModel(
                        HttpStatus.OK,
                        OrdersFeedDto.response(savedOrder),
                        "Order returned successfully"
                );
            }

            return new ResponseModel(
                    HttpStatus.NOT_ACCEPTABLE,
                    null,
                    "Order cannot be returned. Only delivered orders can be returned within 7 days of delivery."
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed to return order"
            );
        }
    }

    @Override
    public ResponseModel updateOrder(Long orderId, OrderStatus orderStatus){
        try {
            Orders existOrder = orderDao.findOrderById(orderId)
                    .orElseThrow(() -> new NoSuchElementException("Order Not Found"));

                if (existOrder.getOrderStatus() == orderStatus){
                    return new ResponseModel(
                            HttpStatus.NOT_MODIFIED,
                            null,
                            "Your Order Already " + existOrder.getOrderStatus()
                    );
                }

                existOrder.setOrderStatus(orderStatus);
                Orders saveOrder = orderDao.saveOrder(existOrder);

                return new ResponseModel(
                        HttpStatus.OK,
                        OrdersFeedDto.response(saveOrder),
                        "Order Status Updated Successfully"
                );


            } catch (NoSuchElementException e){
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        e.getMessage()
                );
        } catch (Exception e){
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed To Update Order"
            );
        }
    }


}

