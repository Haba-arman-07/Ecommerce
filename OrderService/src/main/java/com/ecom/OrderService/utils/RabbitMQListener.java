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

            Map<String, Object> map = objectMapper.readValue(message, Map.class);

            String userName = map.get("UserName").toString();
            String toEmail = map.get("email").toString();
            String subject = "Order Confirm Placed Successfully ";

            List<Map<String, Object>> productList = (List<Map<String, Object>>) map.get("Products");

            StringBuilder builder = new StringBuilder();
            for (Map<String, Object> product : productList) {
                builder.append("""
                        <div style="max-width: 400px; margin: 15px auto; border: 1px solid #ddd; border-radius: 10px; padding: 15px; background: #f9f9f9; font-family: Arial, sans-serif;">
                        
                            <div style="text-align:center; margin-bottom: 15px;">
                                <img src="%s" alt="Product Image" style="max-width:150px; border-radius:8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);" />
                            </div>
                        
                            <div style="padding: 8px 0; display: flex; justify-content: space-between; border-bottom: 1px solid #eee;">
                                <b style="color:#333;">Product Name : </b><pre> </pre>
                                <span style="color:#555;"> %s </span>
                            </div>
                        
                            <div style="padding: 8px 0; display: flex; justify-content: space-between; border-bottom: 1px solid #eee;">
                                <b style="color:#333;">Quantity :</b> <pre> </pre>
                                <span style="color:#555;">%s</span>
                            </div>
                        
                            <div style="padding: 8px 0; display: flex; justify-content: space-between; border-bottom: 1px solid #eee;">
                                <b style="color:#333;">Price :</b> <pre> </pre>
                                <span style="color:#007b5e; font-weight:bold;">&#8377;%s</span>
                            </div>
                        
                            <div style="padding: 8px 0; display: flex; justify-content: space-between;">
                                <b style="color:#333;">Message :</b> <pre> </pre>
                                <span style="color:#555;">%s</span>
                            </div>
                        
                            <p style="font-size: 13px; color: #777; text-align: center; margin-top: 10px;">
                                Thank you for shopping with us!
                            </p>
                        </div>
                        """.formatted(
                        product.get("Image"),
                        product.get("productName"),
                        product.get("Quantity"),
                        product.get("Price"),
                        product.get("Message")
                ));
            }

            // HTML email body
            String body = """
                    <html>
                              <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                                <div style="max-width: 600px; background: #ffffff; border-radius: 8px; padding: 20px; margin: auto; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                    
                                  <h2 style="color: #4CAF50; text-align: center;">Hello, %s!</h2>
                                  <p style="font-size: 16px; text-align: center;">Your order has been placed successfully!</p>
                    
                                  <h3 style="border-bottom: 1px solid #ddd; padding-bottom: 5px;">Order Details</h3>
                    
                                        %s
                    
                                        <!-- CTA -->
                                        <p style="text-align: center; margin-top: 30px;">
                                            <a href="https://www.amazon.in/gp/your-account/order-history" style="background: #FF5722; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                                                View Order
                                            </a>
                                        </p>
                              </body>
                    </html>
                    """.formatted(userName, builder.toString());

            // Send email as HTML
            mailService.sendEmail(toEmail, subject, body, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Cart Reminder
    @RabbitListener(queues = "cart.reminder.queue")
    public void cartReminder(String message) {
        try {
            Map<String, Object> map = objectMapper.readValue(message, Map.class);

            String userName = map.get("UserName").toString();
            String toEmail = map.get("email").toString();
            String subject = "Reminder: Items left in your cart!";

            // Fetch Products list from map
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
                                    <b>Product Name :</b> <pre> </pre>
                                    <span>%s</span>
                                </div>
                        
                                <div style="display:flex; justify-content:space-between; padding:5px 0;">
                                    <b>Quantity :</b> <pre> </pre>
                                    <span>%s</span>
                                </div>
                        
                                <div style="display:flex; justify-content:space-between; padding:5px 0;">
                                    <b>Price :</b> <pre> </pre>
                                    <span>&#8377;%s</span>
                                </div>
                            </div>
                        """.formatted(product.get("Image"), product.get("productName"), product.get("Quantity"), product.get("Price")));
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

    //Cancel Order
    @RabbitListener(queues = "order.cancel.queue")
    public void cancelOrder(String message) {
        try {
            Map<String, Object> map = objectMapper.readValue(message, Map.class);

            String userName = map.get("UserName").toString();
            String toEmail = map.get("email").toString();
            String subject = "Your Order Cancelled Successfully ";

            List<Map<String, Object>> products = (List<Map<String, Object>>) map.get("Products");

            StringBuilder productHtml = new StringBuilder();
            for (Map<String, Object> product : products) {
                productHtml.append("""
                                <div style="border-bottom:1px solid #ddd; padding:10px 0;">
                                    <div style="text-align:center;">
                                        <img src="%s" alt="Product Image" style="max-width:120px; border-radius:5px;" />
                                    </div>
                                    <p><b>Product Name:</b> %s</p>
                                    <p><b>Quantity:</b> %s</p>
                                    <p><b>Price:</b> &#8377;%s</p>
                                </div>
                        """.formatted(
                        product.get("Image"),
                        product.get("productName"),
                        product.get("Quantity"),
                        product.get("Price")));
            }

            String body = """
                            <html>
                                <body style="font-family: Arial; background: #fff5f5; padding: 20px;">
                                    <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:8px; padding:20px; box-shadow:0 2px 5px rgba(0,0,0,0.1);">
                                        <h2 style="color:#FF0000; text-align:center;">Hi %s,</h2>
                                        <p style="text-align:center; font-size:16px;">Your order(s) have been <b style="color:#FF0000;">cancelled successfully</b>.</p>
                                        %s
                                        <p style="text-align:center;">
                                            <a href="https://www.amazon.in" style="background:#FF0000; color:white;margin-top: 5px; padding:10px 20px; border-radius:5px; text-decoration:none;">
                                                Shop Again
                                            </a>
                                        </p>
                                    </div>
                                </body>
                            </html>
                    """.formatted(userName, productHtml.toString());

            mailService.sendEmail(toEmail, subject, body, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}