package com.poianitibaldizhou.trackme.apigateway.message.publisher;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.message.protocol.ThirdPartyProtocolMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class ThirdPartyPublisherImpl implements ThirdPartyPublisher{

    private RabbitTemplate rabbitTemplate;
    private TopicExchange thirdPartyExchange;

    public ThirdPartyPublisherImpl(RabbitTemplate rabbitTemplate, TopicExchange thirdPartyExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.thirdPartyExchange = thirdPartyExchange;
    }

    @Override
    public void publishPrivateThirdPartyCreated(PrivateThirdPartyDetail privateThirdPartyDetail) {
        ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
        thirdPartyProtocolMessage.setId(privateThirdPartyDetail.getThirdPartyCustomer().getId());
        thirdPartyProtocolMessage.setSsn(privateThirdPartyDetail.getSsn());
        thirdPartyProtocolMessage.setName(privateThirdPartyDetail.getName());
        thirdPartyProtocolMessage.setSurname(privateThirdPartyDetail.getSurname());
        thirdPartyProtocolMessage.setBirthCity(privateThirdPartyDetail.getBirthCity());
        thirdPartyProtocolMessage.setBirthDate(privateThirdPartyDetail.getBirthDate());
        rabbitTemplate.convertAndSend(thirdPartyExchange.getName(), "third-party.event.created", thirdPartyProtocolMessage);
        log.info("PUBLISHED PrivateThirdParty: " + thirdPartyProtocolMessage);
    }

    @Override
    public void publishCompanyThirdPartyCreated(CompanyDetail companyDetail) {
        ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
        thirdPartyProtocolMessage.setId(companyDetail.getThirdPartyCustomer().getId());
        thirdPartyProtocolMessage.setCompanyName(companyDetail.getCompanyName());
        thirdPartyProtocolMessage.setAddress(companyDetail.getAddress());
        thirdPartyProtocolMessage.setDunsNumber(companyDetail.getDunsNumber());
        rabbitTemplate.convertAndSend(thirdPartyExchange.getName(), "third-party.event.created", thirdPartyProtocolMessage);
        log.info("PUBLISHED CompanyThirdParty: " + thirdPartyProtocolMessage);
    }
}
