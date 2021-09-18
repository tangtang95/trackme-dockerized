package com.poianitibaldizhou.trackme.individualrequestservice.message.configuration;

import com.poianitibaldizhou.trackme.individualrequestservice.message.publisher.IndividualRequestPublisher;
import com.poianitibaldizhou.trackme.individualrequestservice.message.publisher.IndividualRequestPublisherImpl;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
     * Declare a new bean of IndividualRequestPublisher
     *
     * @param rabbitTemplate the template of rabbitmq
     * @param individualRequestExchange the topic exchange of individual request
     * @return the new object of IndividualRequestPublisher
     */
    @Bean
    public IndividualRequestPublisher individualRequestPublisher(RabbitTemplate rabbitTemplate,
                                                                 TopicExchange individualRequestExchange){
        return new IndividualRequestPublisherImpl(rabbitTemplate, individualRequestExchange);
    }

}
