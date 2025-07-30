package com.ecom.OrderService.utils;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.OrderService.config.RabbitMQConfig;
import com.ecom.OrderService.dao.MasterDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CroneJobSheduler {

    @Autowired
    private MasterDao masterDao;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private ObjectMapper mapper;

    @Scheduled(cron = "*/10 * * * * *")
    public void sendCartReminder() {
        try {
            // Get all users who have active carts
            List<Cart> allUserCarts = masterDao.getCartRepo().findActiveUsersWithCart(Status.ACTIVE);

            // Use a set to avoid sending multiple emails to the same user
            Set<Long> processedUsers = new HashSet<>();

            for (Cart cart : allUserCarts) {
                Users user = cart.getUser();

                // Skip if already processed
                if (processedUsers.contains(user.getUserId())) {
                    continue;
                }
                processedUsers.add(user.getUserId());

                // Get all cart items for the user
                List<Cart> userCartList = masterDao.getCartRepo().findByUserId(user.getUserId());

                // Prepare product list
                List<Map<String, Object>> productList = new ArrayList<>();
                for (Cart userCart : userCartList) {
                    Map<String, Object> productMap = Map.of(
                            "Image", userCart.getProduct().getImageUrl(),
                            "productName", userCart.getProduct().getProductName(),
                            "Quantity", userCart.getQuantity(),
                            "Price", userCart.getPrice()
                    );
                    productList.add(productMap);
                }

                // Final mail data with products list
                Map<String, Object> mailData = new HashMap<>();
                mailData.put("UserName", user.getFirstName() + " " + user.getLastName());
                mailData.put("email", user.getEmail());
                mailData.put("products", productList);

                // Send to RabbitMQ
                rabbitMQProducer.sendMessage(
                        mapper.writeValueAsString(mailData),
                        RabbitMQConfig.ORDER_MAIL_EXCHANGE,
                        RabbitMQConfig.ROUTING_KEY_CART_REMINDER
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
