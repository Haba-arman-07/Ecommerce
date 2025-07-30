package com.ecom.OrderService.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RabbitMQListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MailService mailService;

    @RabbitListener(queues = "order.mail.queue")
    public void placedOrder(String message) {
        try {

            Map<String, String> map = objectMapper.readValue(message, Map.class);

            String toEmail = map.get("email");
            String subject = "Order Confirm: " + map.get("orderId");

            // HTML email body
            String body = """
                    <html>
                              <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                                <div style="max-width: 600px; background: #ffffff; border-radius: 8px; padding: 20px; margin: auto; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                    
                                  <h2 style="color: #4CAF50; text-align: center;">Hello, %s!</h2>
                                  <p style="font-size: 16px; text-align: center;">Your order has been placed successfully!</p>
                    
                                  <h3 style="border-bottom: 1px solid #ddd; padding-bottom: 5px;">Order Details</h3>
                    
                                  <!-- Product Name -->
                                  <div style="display: flex; justify-content: space-between; padding: 8px; border: 0px solid #ddd; border-radius: 5px; margin-bottom: 5px;">
                                    <b>Product Name :</b><pre> </pre>
                                    <span> %s</span>
                                  </div>
                    
                                  <!-- Product Image -->
                                 <div style="text-align: center; padding: 10px;">
                                   <img src="%s" alt="Product Image" style="max-width: 150px; border-radius: 5px;" />
                                 </div>
                    
                                  <!-- Quantity -->
                                  <div style="display: flex; justify-content: space-between; padding: 8px; border: 0px solid #ddd; border-radius: 5px; margin-bottom: 5px;">
                                    <b>Quantity :</b><pre> </pre>
                                    <span>%s</span>
                                  </div>
                    
                                  <!-- Price -->
                                  <div style="display: flex; justify-content: space-between; padding: 8px; border: 0px solid #ddd; border-radius: 5px; margin-bottom: 5px;">
                                    <b>Price :</b><pre> </pre>
                                    <span>&#8377;%s</span>
                                  </div>
                    
                                  <!-- Message -->
                                  <p style="margin-top: 20px;"><b>Message:</b> %s</p>
                                  <p style="font-size: 14px; color: #777; text-align: center;">Thank you for shopping with us!</p>
                                </div>
                              </body>
                    </html>
                    """.formatted(
                    map.get("UserName"),
                    map.get("productName"),
                    map.get("Quantity"),
                    map.get("Price"),
                    map.get("Message")
            );

            // Send email as HTML
            mailService.sendEmail(toEmail, subject, body, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Cart Reminder
    @RabbitListener(queues = "cart.reminder.queue")
    public void cartReminder(String message) {
        try {
            Map<String, Object> map = objectMapper.readValue(message, Map.class);

            String toEmail = (String) map.get("email");
            String userName = (String) map.get("UserName");
            String subject = "Reminder: Items left in your cart!";

            // Fetch products list from map
            List<Map<String, Object>> products = (List<Map<String, Object>>) map.get("products");

            // Build product list HTML dynamically
            StringBuilder productHtml = new StringBuilder();
            for (Map<String, Object> product : products) {
                productHtml.append("""
                <div style="border-bottom:1px solid #ddd; padding:10px 0;">
                    <div style="text-align:center;">
                        <img src="%s" alt="Product Image" style="max-width:120px; border-radius:5px;" />
                    </div>
                    <div style="display:flex; justify-content:space-between; padding:5px 0;">
                        <b>Product Name :</b> <span>%s</span>
                    </div>
                    <div style="display:flex; justify-content:space-between; padding:5px 0;">
                        <b>Quantity :</b> <span>%s</span>
                    </div>
                    <div style="display:flex; justify-content:space-between; padding:5px 0;">
                        <b>Price :</b> <span>&#8377;%s</span>
                    </div>
                </div>
            """.formatted(
                        product.get("Image"),
                        product.get("productName"),
                        product.get("Quantity"),
                        product.get("Price")
                ));
            }

            // Final email body with user name and product list
            String body = """
                <html>
                    <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                        <div style="max-width: 600px; background: #ffffff; border-radius: 8px; padding: 20px; margin: auto; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                            <h2 style="color: #FF5722; text-align: center;">Hi, %s!</h2>
                            <p style="font-size: 16px; text-align: center;">
                                You still have items waiting in your cart. Complete your purchase before they go out of stock!
                            </p>
                            
                            <h3 style="border-bottom: 1px solid #ddd; padding-bottom: 5px;">Cart Details</h3>
                            
                            %s
                            
                            <!-- CTA -->
                            <p style="text-align: center; margin-top: 30px;">
                                <a href="https://www.amazon.in/gp/cart/view.html" style="background: #FF5722; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                                    Complete Your Order
                                </a>
                            </p>
                        </div>
                    </body>
                </html>
        """.formatted(userName, productHtml.toString());

            // Send email as HTML
            mailService.sendEmail(toEmail, subject, body, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}