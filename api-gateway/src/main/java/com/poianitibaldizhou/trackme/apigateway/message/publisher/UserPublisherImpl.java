package com.poianitibaldizhou.trackme.apigateway.message.publisher;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.message.protocol.UserProtocolMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class UserPublisherImpl implements UserPublisher {

    private RabbitTemplate rabbitTemplate;
    private TopicExchange userExchange;

    public UserPublisherImpl(RabbitTemplate rabbitTemplate, TopicExchange userExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.userExchange = userExchange;
    }

    @Override
    public void publishUserCreated(User user) {
        UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
        userProtocolMessage.setSsn(user.getSsn());
        userProtocolMessage.setFirstName(user.getFirstName());
        userProtocolMessage.setLastName(user.getLastName());
        userProtocolMessage.setBirthDate(user.getBirthDate());
        userProtocolMessage.setBirthCity(user.getBirthCity());
        userProtocolMessage.setBirthNation(user.getBirthNation());
        rabbitTemplate.convertAndSend(userExchange.getName(), "user.event.created", userProtocolMessage);
        log.info("PUBLISHED User: " + userProtocolMessage);
    }

}
