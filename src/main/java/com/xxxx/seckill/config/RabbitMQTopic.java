//package com.xxxx.seckill.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQTopic {
//    private static final String QUEUE_topic01 = "queue_topic1";
//    private static final String QUEUE_topic02 = "queue_topic2";
//    private static final String TOPICEXCHANGE = "topic_exchange";
//    private static final String ROUTING1 = "#.queue.#";//*代表只能匹配一个，#代表能匹配0个或多个
//    private static final String ROUTING2 = "*.queue.*";
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE_topic01);
//    }
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE_topic02);
//    }
//    @Bean
//    public TopicExchange exchange(){
//        return new TopicExchange(TOPICEXCHANGE);
//    }
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(exchange()).with(ROUTING1);
//    }
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(exchange()).with(ROUTING2);
//    }
//}
