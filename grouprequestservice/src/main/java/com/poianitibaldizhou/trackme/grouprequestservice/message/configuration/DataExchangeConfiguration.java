package com.poianitibaldizhou.trackme.grouprequestservice.message.configuration;

import com.poianitibaldizhou.trackme.grouprequestservice.message.listener.NumberOfUserInvolvedDataEventListener;
import com.poianitibaldizhou.trackme.grouprequestservice.message.listener.NumberOfUserInvolvedDataEventListenerImpl;
import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
     * Create the number of user involved data queue listener to receive messages regarding the generation of
     * number of user involved data
     *
     * @param internalCommunicationService the service handling the internal communication message from other services
     * @return the number of user involved data queue listener
     */
    @Bean
    public NumberOfUserInvolvedDataEventListener numberOfUserInvolvedDataEventListener(
            InternalCommunicationService internalCommunicationService){
        return new NumberOfUserInvolvedDataEventListenerImpl(internalCommunicationService);
    }


}
