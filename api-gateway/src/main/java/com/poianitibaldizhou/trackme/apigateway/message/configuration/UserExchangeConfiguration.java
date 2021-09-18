package com.poianitibaldizhou.trackme.apigateway.message.configuration;

import com.poianitibaldizhou.trackme.apigateway.message.publisher.UserPublisher;
import com.poianitibaldizhou.trackme.apigateway.message.publisher.UserPublisherImpl;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
     * Declare a new bean of user publisher that manages the publish of user messages.
     *
     * @param rabbitTemplate the template of rabbit to interact with the server rabbitmq
     * @param userExchange the topic exchange of the user
     * @return a user publisher
     */
    @Bean
    public UserPublisher userPublisher(RabbitTemplate rabbitTemplate, TopicExchange userExchange){
        return new UserPublisherImpl(rabbitTemplate, userExchange);
    }

}
