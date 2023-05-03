package ru.tinkoff.edu.scrapper.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue queue(ApplicationProperties properties) {
        return QueueBuilder
                .durable(properties.rabbitMq().queueName())
                .withArgument("x-dead-letter-exchange", properties.rabbitMq().queueName() + ".dlx")
                .build();
    }

    @Bean
    public TopicExchange exchange(ApplicationProperties properties) {
        return new TopicExchange(properties.rabbitMq().topicExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange, ApplicationProperties properties) {
        return BindingBuilder.bind(queue).to(exchange).with(properties.rabbitMq().linksRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // FIXME?
    // Rabbitmq-starter declares queues lazily.
    // If there's no links yet, links queue won't be created.
    // Bot begins listening for links queue on start
    // But there's no links queue, so it fails to startup
    // That's why we declare queue manually
    @Bean
    public RabbitAdmin rabbitAdmin(
            ConnectionFactory connectionFactory,
            TopicExchange exchange,
            Queue queue,
            Binding binding
    ) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);

        return rabbitAdmin;
    }
}
