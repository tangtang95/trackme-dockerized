package com.poianitibaldizhou.trackme.individualrequestservice.message.configuration;

import com.poianitibaldizhou.trackme.individualrequestservice.message.listener.UserEventListener;
import com.poianitibaldizhou.trackme.individualrequestservice.message.listener.UserEventListenerImpl;
import com.poianitibaldizhou.trackme.individualrequestservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The class configuration of user exchange and its queues
 */
@Profile("usage-message-broker")
@Configuration
public class UserExchangeConfiguration {

    /**
     * Declare a new topic exchange regarding users. If it already exists on the rabbitmq server, then it does nothing.
     *
     * @return the topic exchange regarding users
     */
    @Bean
    public TopicExchange userExchange(){
        return new TopicExchange(Constants.USER_EXCHANGE_NAME);
    }

    /**
     * Declare a new queue regarding users created to send it to individual request service.
     * If it already exists, it does nothing
     *
     * @return the queue regarding the users created for individual request service
     */
    @Bean
    public Queue userCreatedToIndividualRequestServiceQueue(){
        return new Queue(Constants.USER_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME);
    }

    /**
     * Declare a new binding between userExchange and the queue of created user for individual request service.
     * If it already exists, it does nothing
     *
     * @param userExchange the bean of user exchange
     * @param userCreatedToIndividualRequestServiceQueue the bean of the queue of created user for individual request service
     * @return the binding between userExchange and the queue of created user for individual request data service
     */
    @Bean
    public Binding bindUserExchangeToAcceptedIndividualRequestQueue(TopicExchange userExchange,
                                                               Queue userCreatedToIndividualRequestServiceQueue){
        return BindingBuilder.bind(userCreatedToIndividualRequestServiceQueue).to(userExchange)
                .with("user.*.created");
    }

    /**
     * Declare a new bean of UserEventListener
     *
     * @param internalCommunicationService the service of internal communication between microservices
     * @return the new object of UserEventListener
     */
    @Bean
    public UserEventListener userEventListener(InternalCommunicationService internalCommunicationService){
        return new UserEventListenerImpl(internalCommunicationService);
    }

}
