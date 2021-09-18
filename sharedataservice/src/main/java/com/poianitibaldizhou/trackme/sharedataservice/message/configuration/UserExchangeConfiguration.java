package com.poianitibaldizhou.trackme.sharedataservice.message.configuration;

import com.poianitibaldizhou.trackme.sharedataservice.message.listener.UserEventListener;
import com.poianitibaldizhou.trackme.sharedataservice.message.listener.UserEventListenerImpl;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
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
     * Declare a new queue regarding users created to send it to share data service.
     * If it already exists, it does nothing
     *
     * @return the queue regarding the users created for share data service
     */
    @Bean
    public Queue userCreatedToShareDataServiceQueue(){
        return new Queue(Constants.USER_CREATED_SHARE_DATA_QUEUE_NAME);
    }

    /**
     * Declare a new binding between userExchange and the queue of created user for share data service.
     * If it already exists, it does nothing
     *
     * @param userExchange the bean of user exchange
     * @param userCreatedToShareDataServiceQueue the bean of the queue of created user for share data service
     * @return the binding between userExchange and the queue of created user for share data service
     */
    @Bean
    public Binding bindUserExchangeToCreatedShareDataQueue(TopicExchange userExchange,
                                                               Queue userCreatedToShareDataServiceQueue){
        return BindingBuilder.bind(userCreatedToShareDataServiceQueue).to(userExchange)
                .with("user.*.created");
    }

    /**
     * Create the user queue listener to receive messages regarding the user exchange's queues
     * @param internalCommunicationService the service handling the internal communication message from other services
     * @return the user queue listener
     */
    @Bean
    public UserEventListener userEventListener(InternalCommunicationService internalCommunicationService){
        return new UserEventListenerImpl(internalCommunicationService);
    }

}
