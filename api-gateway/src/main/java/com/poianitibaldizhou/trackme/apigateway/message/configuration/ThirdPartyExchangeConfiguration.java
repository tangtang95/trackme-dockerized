package com.poianitibaldizhou.trackme.apigateway.message.configuration;

import com.poianitibaldizhou.trackme.apigateway.message.publisher.ThirdPartyPublisher;
import com.poianitibaldizhou.trackme.apigateway.message.publisher.ThirdPartyPublisherImpl;
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
 * The class configuration of third party exchange and its queues
 */
@Profile("usage-message-broker")
@Configuration
public class ThirdPartyExchangeConfiguration {

    /**
     * Declare a new topic exchange regarding third parties. If it already exists on the rabbitmq server, then it does nothing.
     *
     * @return the topic exchange regarding third parties
     */
    @Bean
    public TopicExchange thirdPartyExchange(){
        return new TopicExchange(Constants.THIRD_PARTY_EXCHANGE_NAME);
    }

    /**
     * Declare a new queue regarding third parties created to send it to share data service.
     * If it already exists, it does nothing
     *
     * @return the queue regarding the third parties created for share data service
     */
    @Bean
    public Queue thirdPartyCreatedToIndividualRequestServiceQueue(){
        return new Queue(Constants.THIRD_PARTY_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME);
    }

    /**
     * Declare a new binding between thirdPartyExchange and the queue of created third party for share data service.
     * If it already exists, it does nothing
     *
     * @param thirdPartyExchange the bean of third party exchange
     * @param thirdPartyCreatedToIndividualRequestServiceQueue the bean of the queue of created third party for share data service
     * @return the binding between thirdPartyExchange and the queue of created third party for share data service
     */
    @Bean
    public Binding bindThirdPartyExchangeToCreatedIndividualRequestQueue(TopicExchange thirdPartyExchange,
                                                           Queue thirdPartyCreatedToIndividualRequestServiceQueue){
        return BindingBuilder.bind(thirdPartyCreatedToIndividualRequestServiceQueue).to(thirdPartyExchange)
                .with("third-party.*.created");
    }

    /**
     * Declare a new bean of third party publisher that manages the publish of third party messages.
     *
     * @param rabbitTemplate the template of rabbit to interact with the server rabbitmq
     * @param thirdPartyExchange the topic exchange of the third party
     * @return a third party publisher
     */
    @Bean
    public ThirdPartyPublisher thirdPartyPublisher(RabbitTemplate rabbitTemplate, TopicExchange thirdPartyExchange){
        return new ThirdPartyPublisherImpl(rabbitTemplate, thirdPartyExchange);
    }

}
