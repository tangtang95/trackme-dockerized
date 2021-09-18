package com.poianitibaldizhou.trackme.sharedataservice.message.configuration;

import com.poianitibaldizhou.trackme.sharedataservice.message.publisher.NumberOfUserInvolvedDataPublisher;
import com.poianitibaldizhou.trackme.sharedataservice.message.publisher.NumberOfUserInvolvedDataPublisherImpl;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The class configuration of data exchange and its queues
 */
@Profile("usage-message-broker")
@Configuration
public class DataExchangeConfiguration {

    /**
     * Declare a new topic exchange regarding number of user involved. If already
     * existing on the rabbitmq server it does nothing.
     *
     * @return the topic exchange regarding the number of user involved
     */
    @Bean
    public TopicExchange numberOfUserInvolvedExchange(){
        return new TopicExchange(Constants.NUMBER_OF_USER_INVOLVED_EXCHANGE_NAME);
    }

    /**
     * Declare a new queue regarding the number of user involved to send to group request service.
     * If it already exists, it does nothing
     *
     * @return the queue regarding the number of user involved for group request service
     */
    @Bean
    public Queue numberOfUserInvolvedGeneratedToGroupRequestServiceQueue(){
        return new Queue(Constants.NUMBER_OF_USER_INVOLVED_GENERATED_GROUP_REQUEST_QUEUE_NAME);
    }

    /**
     * Declare a new binding between numberOfUSerInvolvedExchange and the queue for group request service.
     * If it already exists, it does nothing
     *
     * @param numberOfUserInvolvedExchange the bean of numberOfUserInvolvedExchange
     * @param numberOfUserInvolvedGeneratedToGroupRequestServiceQueue the bean of the queue for group request service
     * @return the binding between numberOfUSerInvolvedExchange and the queue of group request service
     */
    @Bean
    public Binding bindNumberOfUserInvolvedExchangeToGeneratedGroupRequestQueue(TopicExchange numberOfUserInvolvedExchange,
                                                                                Queue numberOfUserInvolvedGeneratedToGroupRequestServiceQueue){
        return BindingBuilder.bind(numberOfUserInvolvedGeneratedToGroupRequestServiceQueue).to(numberOfUserInvolvedExchange)
                .with("number-of-user.*.generated");
    }

    /**
     * Create a new bean of the publisher of number of user involved data
     *
     * @param rabbitTemplate the template of rabbit to send data
     * @param numberOfUserInvolvedExchange the topic exchange of number of user involved
     * @return the publisher of number of user involved data
     */
    @Bean
    public NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher(RabbitTemplate rabbitTemplate,
                                                                               TopicExchange numberOfUserInvolvedExchange){
        return new NumberOfUserInvolvedDataPublisherImpl(rabbitTemplate, numberOfUserInvolvedExchange);
    }


}
