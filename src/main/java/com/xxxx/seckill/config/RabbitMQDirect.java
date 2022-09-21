//package com.xxxx.seckill.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQDirect {
//    private static final String QUEUE01 = "queue_Direct01";
//    private static final String QUEUE02 = "queue_Direct02";
//    private static final String EXCHANGE_DIRECT = "direct_exchange";
//    private static final String ROUTINGKEY01 = "rout_red";
//    private static final String ROUTINGKEY02 = "rout_green";
//    @Bean
//    public Queue setQueue01(){
//        return new Queue (QUEUE01);
//    }
//    @Bean
//    public Queue setQueue02(){
//        return new Queue(QUEUE02);
//    }
//    @Bean
//    public DirectExchange setExchange(){
//        return new DirectExchange(EXCHANGE_DIRECT);
//    }
//    @Bean
//    public Binding setBind01(){
//        return BindingBuilder.bind(setQueue01()).to(setExchange()).with(ROUTINGKEY01);
//    }
//    @Bean
//    public Binding setBind02(){
//        return BindingBuilder.bind(setQueue02()).to(setExchange()).with(ROUTINGKEY02);
//    }
//}
