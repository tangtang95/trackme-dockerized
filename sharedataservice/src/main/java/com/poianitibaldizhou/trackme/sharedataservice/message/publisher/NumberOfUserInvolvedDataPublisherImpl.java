package com.poianitibaldizhou.trackme.sharedataservice.message.publisher;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.NumberOfUserInvolvedProtocolMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class NumberOfUserInvolvedDataPublisherImpl implements NumberOfUserInvolvedDataPublisher {

    private RabbitTemplate rabbitTemplate;

    private TopicExchange numberOfUserInvolvedExchange;

    public NumberOfUserInvolvedDataPublisherImpl(RabbitTemplate rabbitTemplate,
                                                 TopicExchange numberOfUserInvolvedExchange){
        this.rabbitTemplate = rabbitTemplate;
        this.numberOfUserInvolvedExchange = numberOfUserInvolvedExchange;
    }

    @Override
    public void publishNumberOfUserInvolvedData(Long groupRequestId, Integer numberOfUserInvolved) {
        NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
        protocolMessage.setNumberOfUserInvolved(numberOfUserInvolved);
        protocolMessage.setGroupRequestId(groupRequestId);
        rabbitTemplate.convertAndSend(numberOfUserInvolvedExchange.getName(),"number-of-user.event.generated", protocolMessage);
        log.info("PUBLISHED NumberOfUserInvolvedData: " + protocolMessage);
    }
}
