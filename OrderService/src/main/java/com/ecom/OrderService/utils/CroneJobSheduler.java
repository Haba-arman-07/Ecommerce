package com.ecom.OrderService.utils;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.CartItem;
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

    @Scheduled(cron = "0 0 */1 * * *")
    public void sendCartReminder() {
        try {
            List<Cart> allUserCarts = masterDao.getCartRepo().findActiveUsersWithCart(Status.ACTIVE);

            Set<Long> processedUsers = new HashSet<>();

            for (Cart cart : allUserCarts) {
                Users user = cart.getUser();

                if (processedUsers.contains(user.getUserId())) {
                    continue;
                }

                processedUsers.add(user.getUserId());

//              Fetch all cart items for this user's cart
                List<CartItem> cartItems = masterDao.getCartItemsRepo()
                        .findByCart_CartId(cart.getCartId());

//                List<Cart> userCartList = masterDao.getCartRepo().findByUserId(user.getUserId());

                List<Map<String, String>> productList = new ArrayList<>();
                for (CartItem item : cartItems) {
                    Map<String, String> productMap = Map.of(
                            "Image", item.getProduct().getImageUrl(),
                            "productName", item.getProduct().getProductName(),
                            "Quantity", String.valueOf(item.getQuantity()),
                            "Price", String.valueOf(item.getPrice())
                    );
                    productList.add(productMap);
                }

                // Final mail data with a product list
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
