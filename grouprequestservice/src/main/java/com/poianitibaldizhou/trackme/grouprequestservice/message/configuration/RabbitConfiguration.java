package com.poianitibaldizhou.trackme.grouprequestservice.message.configuration;

import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerServiceImpl;
import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationService;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * The class configuration of the message broker rabbitmq
 */
@Profile("usage-message-broker")
@Configuration
public class RabbitConfiguration {

    /**
     * Declare a bean of Jackson2JsonMessageConverter
     *
     * @return an object of Jackson2JsonMessageConverter
     */
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Declare a new container factory of simple rabbit listener with the message converter of Jackson2Json
     *
     * @param connectionFactory the connection factory of rabbitmq
     * @param configurer the factory configurer of simple rabbit listener
     *
     * @return the new container factory of simple rabbit listener
     */
    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
                                                           SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    /**
     * Set the InternalCommunicationService object to GroupRequestManagerService @Service only when the active profile is
     * usage-message-broker
     *
     * @param groupRequestManagerService the bean service of group request manager
     * @param internalCommunicationService the internal communication service bean
     * @return the command line runner which sets the internalCommunicationService
     */
    @Bean
    public CommandLineRunner setInternalCommunicationService(GroupRequestManagerServiceImpl groupRequestManagerService,
                                                             InternalCommunicationService internalCommunicationService){
        return args -> groupRequestManagerService.setInternalCommunicationService(internalCommunicationService);
    }

    /**
     * Set the message converter of Jackson2Json to the rabbitTemplate bean
     * @param rabbitTemplate the bean of rabbit template
     * @return the command line runner which sets the message converter
     */
    @Bean
    public CommandLineRunner setMessageConverter(RabbitTemplate rabbitTemplate){
        return args -> rabbitTemplate.setMessageConverter(jsonMessageConverter());
    }


}
