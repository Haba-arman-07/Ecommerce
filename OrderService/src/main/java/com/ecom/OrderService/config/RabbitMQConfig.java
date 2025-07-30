package com.ecom.OrderService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Exchange share (Orders, Cart)
    public static final String ORDER_MAIL_EXCHANGE = "order.mail.exchange";

    //Order Mail
    public static final String ROUTING_KEY_ORDER_MAIL = "order.mail.send";
    public static final String QUEUE_ORDER_MAIL = "order.mail.queue";

    //Cart Mail
    public static final String ROUTING_KEY_CART_REMINDER = "cart.reminder.key";
    public static final String QUEUE_CART_REMINDER = "cart.reminder.queue";


    // Order Bean
    @Bean(name = "orderMailExchange")
    public DirectExchange orderMailExchange() {
        return new DirectExchange(ORDER_MAIL_EXCHANGE);
    }

    @Bean(name = "orderMailQueue")
    public Queue orderMailQueue() {
        return new Queue(QUEUE_ORDER_MAIL, false);
    }

    @Bean
    public Binding orderMailBinding(
            @Qualifier("orderMailQueue") Queue Queue,
            @Qualifier("orderMailExchange") DirectExchange Exchange) {
        return BindingBuilder.bind(Queue)
                .to(Exchange)
                .with(ROUTING_KEY_ORDER_MAIL);
    }


    // Cart Bean
    @Bean(name = "cartReminderQueue")
    public Queue cartReminderQueue() {
        return new Queue(QUEUE_CART_REMINDER, false);
    }

    @Bean
    public Binding cartBinding(
            @Qualifier("cartReminderQueue") Queue Queue,
            @Qualifier("orderMailExchange") DirectExchange Exchange) {
        return BindingBuilder.bind(Queue)
                .to(Exchange)
                .with(ROUTING_KEY_CART_REMINDER);
    }

}
