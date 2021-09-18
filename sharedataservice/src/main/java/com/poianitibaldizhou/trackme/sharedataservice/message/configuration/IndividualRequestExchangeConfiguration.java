package com.poianitibaldizhou.trackme.sharedataservice.message.configuration;

import com.poianitibaldizhou.trackme.sharedataservice.message.listener.IndividualRequestEventListener;
import com.poianitibaldizhou.trackme.sharedataservice.message.listener.IndividualRequestEventListenerImpl;
import com.poianitibaldizhou.trackme.sharedataservice.repository.IndividualRequestRepository;
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
 * The class configuration of individual request exchange and its queues
 */
@Profile("usage-message-broker")
@Configuration
public class IndividualRequestExchangeConfiguration {


    /**
     * Declare a new topic exchange regarding individual requests. If it already
     * exists on the rabbitmq server, then it does nothing.
     *
     * @return the topic exchange regarding individual requests
     */
    @Bean
    public TopicExchange individualRequestExchange(){
        return new TopicExchange(Constants.INDIVIDUAL_REQUEST_EXCHANGE_NAME);
    }

    /**
     * Declare a new queue regarding individual requests accepted to send it to share data service.
     * If it already exists, it does nothing
     *
     * @return the queue regarding the individual request accepted for share data service
     */
    @Bean
    public Queue individualRequestAcceptedToShareDataServiceQueue(){
        return new Queue(Constants.INDIVIDUAL_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME);
    }

    /**
     * Declare a new binding between individualRequestExchange and the queue of accepted individual request for share
     * data service. If it already exists, it does nothing
     *
     * @param individualRequestExchange the bean of individual request exchange
     * @param individualRequestAcceptedToShareDataServiceQueue the bean of the queue of accepted individual request
     *                                                         for share data service
     * @return the binding between individualRequestExchange and the queue of accepted individual request for share
     * data service
     */
    @Bean
    public Binding bindIndividualRequestExchangeToAcceptedShareDataQueue(TopicExchange individualRequestExchange,
                                                        Queue individualRequestAcceptedToShareDataServiceQueue){
        return BindingBuilder.bind(individualRequestAcceptedToShareDataServiceQueue).to(individualRequestExchange)
                .with("individualrequest.*.accepted");
    }

    /**
     * Create the individual request queue listener to receive messages regarding the individual request exchange's queue
     * @param internalCommunicationService the service handling the internal communication message from other services
     * @return the individual request queue listener
     */
    @Bean
    public IndividualRequestEventListener individualRequestEventListener(InternalCommunicationService internalCommunicationService){
        return new IndividualRequestEventListenerImpl(internalCommunicationService);
    }

}
