package com.poianitibaldizhou.trackme.individualrequestservice.message.publisher;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatusUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

@Slf4j
public class IndividualRequestPublisherImpl implements IndividualRequestPublisher{

    private RabbitTemplate rabbitTemplate;

    private TopicExchange individualRequestExchange;

    public IndividualRequestPublisherImpl(RabbitTemplate rabbitTemplate, TopicExchange individualRequestExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.individualRequestExchange = individualRequestExchange;
    }

    @Override
    public void publishIndividualRequest(IndividualRequest individualRequest) {
        IndividualRequestProtocolMessage protocolMessage = new IndividualRequestProtocolMessage();
        protocolMessage.setId(individualRequest.getId());
        protocolMessage.setCreationTimestamp(individualRequest.getTimestamp());
        protocolMessage.setThirdPartyId(individualRequest.getThirdParty().getId());
        protocolMessage.setThirdPartyName(individualRequest.getThirdParty().getIdentifierName());
        protocolMessage.setStatus(IndividualRequestStatusUtils.getIndividualRequestStatusOfProtocol(individualRequest.getStatus()));
        protocolMessage.setStartDate(individualRequest.getStartDate());
        protocolMessage.setEndDate(individualRequest.getEndDate());
        protocolMessage.setUserSsn(individualRequest.getUser().getSsn());
        protocolMessage.setMotivationText(individualRequest.getMotivation());
        rabbitTemplate.convertAndSend(individualRequestExchange.getName(), getRoutingKey(protocolMessage.getStatus()),
                protocolMessage);
        log.info("PUBLISHED IndividualRequestProtocolMessage: " + protocolMessage);
    }

    /**
     * Return the routing key based on the status of the individual request protocol message
     *
     * @param statusProtocolMessage the status of the individual request of protocol message
     * @return the routing key based on the status of the individual request protocol
     */
    private String getRoutingKey(IndividualRequestStatusProtocolMessage statusProtocolMessage){
        Map<IndividualRequestStatusProtocolMessage, String> routingKeyMap = ImmutableMap
                .<IndividualRequestStatusProtocolMessage, String>builder()
                .put(IndividualRequestStatusProtocolMessage.ACCEPTED, "individualrequest.event.accepted")
                .put(IndividualRequestStatusProtocolMessage.REFUSED, "individualrequest.event.refused")
                .put(IndividualRequestStatusProtocolMessage.PENDING, "individualrequest.event.pending")
                .build();
        return routingKeyMap.get(statusProtocolMessage);
    }

}
