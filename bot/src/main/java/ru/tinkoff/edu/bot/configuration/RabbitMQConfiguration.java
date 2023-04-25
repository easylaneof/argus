package ru.tinkoff.edu.bot.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.bot.dto.LinkUpdate;

import java.util.Map;

@Configuration
public class RabbitMQConfiguration {
    private static final String DLX_SUFFIX = ".dlx";

    @Bean
    public Queue deadLetterQueue(ApplicationProperties properties) {
        return new Queue(properties.scrapperQueue().dlqName());
    }

    @Bean
    public FanoutExchange deadLetterExchange(ApplicationProperties properties) {
        return new FanoutExchange(properties.scrapperQueue().name() + DLX_SUFFIX);
    }

    @Bean
    public Binding deadLetterBinding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = Map.of(
                "ru.tinkoff.edu.scrapper.client.bot.LinkUpdateRequest", LinkUpdate.class
        );

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.scrapper.client.bot.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }
}
