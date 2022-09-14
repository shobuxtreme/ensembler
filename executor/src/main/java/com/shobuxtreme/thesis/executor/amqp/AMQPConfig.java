package com.shobuxtreme.thesis.executor.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AMQPConfig {

    public static final String QUEUE_ALL_JOBS = "jobs.all";

    public static final String QUEUE_ALL_RESULTS = "results.all";
    public static final String EXCHANGE_JOBS = "ensembler.jobs";
    public static final String ROUTING_KEY_ALL_JOBS = "jobs.*";

    public static final String ROUTING_KEY_ALL_RESULTS = "results.all";

    @Value("${spring.rabbitmq.host}")
    private String amqpHost;
    @Value("${spring.rabbitmq.port}")
    private int amqpPort;

    @Bean
    public Queue queueAllJobs() {
        return  new Queue(QUEUE_ALL_JOBS);
    }

    @Bean
    public Queue queueAllResults() {
        return  new Queue(QUEUE_ALL_RESULTS);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_JOBS);
    }

    @Bean
    public Binding bindingAllJobs() {
        return BindingBuilder
                .bind(queueAllJobs())
                .to(exchange())
                .with(ROUTING_KEY_ALL_JOBS);
    }

    @Bean
    public Binding bindingAllResults() {
        return BindingBuilder
                .bind(queueAllResults())
                .to(exchange())
                .with(ROUTING_KEY_ALL_RESULTS);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(amqpHost);
        connectionFactory.setPort(amqpPort);
        return connectionFactory;
    }

    @Bean(name="rabbitAdmin")
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean(name="rabbitTemplate")
    public RabbitTemplate template() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(messageConverter());
        return  template;
    }
}
