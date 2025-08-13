package com.ecom.OrderService.services.impl;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.OrderResponseDto;
import com.ecom.CommonEntity.entities.*;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.OrderService.config.RabbitMQConfig;
import com.ecom.OrderService.dao.MasterDao;
import com.ecom.OrderService.dao.OrderDao;
import com.ecom.OrderService.services.ServiceInterface.OrderService;
import com.ecom.OrderService.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MasterDao masterDao;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Override
    @Transactional
    public ResponseModel addOrder(Long userId, Long addressId) {
        try {
            if (userId == null || addressId == null) {
                return new ResponseModel(HttpStatus.BAD_REQUEST, null, "User ID and Address ID must not be null.");
            }

            Users user = masterDao.getUserRepo()
                    .findByUserIdAndStatus(userId, Status.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));

            Address address = masterDao.getAddressRepo()
                    .findByUser_UserIdAndAddressId(userId, addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Valid address not found for user."));

            Cart cart = masterDao.getCartRepo()
                    .findByUser_UserIdAndUser_Status(userId, Status.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException("Cart not found."));

            List<CartItem> cartItems = masterDao.getCartItemsRepo().findByCart_CartId(cart.getCartId());

            for (CartItem item : cartItems) {
                Product product = masterDao.getProductRepo()
                        .findByProductIdAndStatus(item.getProduct().getProductId(), Status.ACTIVE)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: ID " + item.getProduct().getProductId()));

                if (product.getQty() < item.getQuantity()) {
                    return new ResponseModel(
                            HttpStatus.BAD_REQUEST,
                            null,
                            "Insufficient stock for product: " + product.getProductName()
                    );
                }
            }

            Orders orders = Orders.builder()
                    .user(user)
                    .address(address)
                    .totalAmount(cart.getTotalAmount())
                    .orderStatus(OrderStatus.PENDING)
                    .status(Status.ACTIVE)
                    .build();

            orderDao.saveOrder(orders);

            List<OrderItem> itemList = new ArrayList<>();
            for (CartItem item : cartItems) {
                Product product = masterDao.getProductRepo()
                        .findByProductIdAndStatus(item.getProduct().getProductId(), Status.ACTIVE)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found."));

                    int quantity = product.getQty() - item.getQuantity();
                    product.setQty(quantity);
                    masterDao.getProductRepo().save(product);

                    OrderItem orderItem = OrderItem.builder()
                            .orders(orders)
                            .product(product)
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build();

                    itemList.add(orderItem);
                }

            List<OrderItem> savedItems = orderDao.saveAllOrderItem(itemList);

//            masterDao.getCartItemsRepo().deleteAll(cartItems);
//            masterDao.getCartRepo().delete(cart);

            List<OrderResponseDto> responseDtos = savedItems.stream()
                    .map(OrderResponseDto::responseDto)
                    .toList();

            List<Map<String, String>> productList = responseDtos.stream()
                    .map(dto -> Map.of(
                            "OrderId", String.valueOf(dto.getOrderId()),
                            "ProductName", dto.getProductName(),
                            "Quantity", String.valueOf(dto.getQuantity()),
                            "Price", String.valueOf(dto.getTotalAmount()),
                            "Message", dto.getOrderStatus().toString()
                    )).toList();

            Map<String, Object> messageMap = Map.of(
                    "UserName", user.getFirstName() + " " + user.getLastName(),
                    "Email", user.getEmail(),
                    "Products", productList
            );

            try {
                String jsonMessage = new ObjectMapper().writeValueAsString(messageMap);

                rabbitMQProducer.sendMessage(
                        jsonMessage,
                        RabbitMQConfig.ORDER_MAIL_EXCHANGE,
                        RabbitMQConfig.ROUTING_KEY_ORDER_MAIL
                );
            } catch (Exception e) {
                System.err.println("Failed to send RabbitMQ message: " + e.getMessage());
            }

            return new ResponseModel(HttpStatus.OK, responseDtos, "Order(s) placed successfully");

        } catch (IllegalArgumentException e) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public ResponseModel getAllOrder() {
        try {
            List<OrderResponseDto> orders = orderDao.findAllOrder()
                    .stream()
                    .map(OrderResponseDto::responseDto)
                    .toList();

            return new ResponseModel(
                    HttpStatus.OK,
                    orders,
                    "Orders fetched successfully"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "An error occurred while fetching orders: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel getUserOrder(Long userId) {
        try {
            if (userId == null) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "User ID must not be null."
                );
            }

            List<OrderResponseDto> orderList = orderDao
                    .findByUserIdAndStatusOrderItem(userId, Status.ACTIVE)
                    .stream()
                    .map(OrderResponseDto::responseDto)
                    .toList();

            if (orderList.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "No active orders found for this user."
                );
            }

            return new ResponseModel(
                    HttpStatus.OK,
                    orderList,
                    "User's active orders fetched successfully."
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "An unexpected error occurred while fetching user orders: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel getOrderItem(Long orderItemId) {
        try {
            OrderItem orderItemOptional = masterDao.getOrderItemRepo()
                    .findById(orderItemId).orElseThrow(() -> new IllegalArgumentException("Order item not found."));

            OrderResponseDto responseDto = OrderResponseDto.responseDto(orderItemOptional);

            return new ResponseModel(
                    HttpStatus.OK,
                    responseDto,
                    "Order item retrieved successfully"
            );

        } catch (IllegalArgumentException e) {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    e.getMessage()
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "An unexpected error occurred while fetching the order item."
            );
        }
    }

    @Override
    public ResponseModel updateOrder(Long orderItemId, OrderStatus orderStatus) {

        try {
            OrderItem existOrder = masterDao.getOrderItemRepo().findById(orderItemId)
                    .orElseThrow(() -> new NoSuchElementException("Order Not Found"));

            if (existOrder.getOrderStatus() == orderStatus) {
                return new ResponseModel(
                        HttpStatus.NOT_MODIFIED,
                        null,
                        "Order Status Already " + orderStatus
                );
            }

            existOrder.setOrderStatus(orderStatus);
            OrderItem orders = orderDao.saveOrderItem(existOrder);


            return new ResponseModel(
                    HttpStatus.OK,
                    OrderResponseDto.responseDto(orders),
                    "Order Update Successfully"
            );
        } catch (NoSuchElementException e) {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    e.getMessage()
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "An unexpected error occurred while update the order."
            );
        }
    }

    @Override
    public ResponseModel cancelOrderItem(Long orderItemId) {
        try {

            OrderItem existingOrder = masterDao.getOrderItemRepo().findById(orderItemId)
                    .orElseThrow(() -> new NoSuchElementException("Order not found with ID: "));

            if (existingOrder.getOrderStatus() == OrderStatus.CANCELLED) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Order is already cancelled."
                );
            }

            if (existingOrder.getOrderStatus() == OrderStatus.PENDING ||
                    existingOrder.getOrderStatus() == OrderStatus.PROCESSING) {

                existingOrder.setOrderStatus(OrderStatus.CANCELLED);
                existingOrder.setUpdatedAt(LocalDateTime.now());
                OrderItem updatedOrder = orderDao.saveOrderItem(existingOrder);

                return new ResponseModel(
                        HttpStatus.OK,
                        OrderResponseDto.responseDto(updatedOrder),
                        "Order cancelled successfully."
                );

            }

            return new ResponseModel(
                    HttpStatus.NOT_ACCEPTABLE,
                    null,
                    existingOrder.getOrderStatus() + " order cannot be cancelled."
            );

        } catch (NoSuchElementException e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    e.getMessage()
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Unexpected error occurred while cancelling the order."
            );
        }
    }

    @Override
    public ResponseModel returnOrder(Long orderItemId) {
        try {
            OrderItem existingOrder = masterDao.getOrderItemRepo().findById(orderItemId)
                    .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderItemId));

            OrderStatus currentStatus = existingOrder.getOrderStatus();

            if (currentStatus == OrderStatus.RETURN) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Order is already returned."
                );
            }

            if (currentStatus != OrderStatus.DELIVERED) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Only delivered orders are eligible for return."
                );
            }


            if (existingOrder.getUpdatedAt().plusDays(7).isAfter(LocalDateTime.now())) {

                existingOrder.setOrderStatus(OrderStatus.RETURN);
                OrderItem returnedOrder = orderDao.saveOrderItem(existingOrder);

                return new ResponseModel(
                        HttpStatus.OK,
                        OrderResponseDto.responseDto(returnedOrder),
                        "Order returned successfully."
                );
            } else {
                return new ResponseModel(
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "Return period has expired. Only orders delivered within the last 7 days can be returned."
                );
            }


        } catch (NoSuchElementException e) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, e.getMessage());
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Unexpected error occurred while processing the return."
            );
        }
    }
}