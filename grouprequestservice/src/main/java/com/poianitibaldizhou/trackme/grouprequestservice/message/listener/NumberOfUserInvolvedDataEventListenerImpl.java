package com.poianitibaldizhou.trackme.grouprequestservice.message.listener;

import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class NumberOfUserInvolvedDataEventListenerImpl implements NumberOfUserInvolvedDataEventListener{

    private InternalCommunicationService internalCommunicationService;

    public NumberOfUserInvolvedDataEventListenerImpl(InternalCommunicationService internalCommunicationService) {
        this.internalCommunicationService = internalCommunicationService;
    }

    @RabbitListener(queues = Constants.NUMBER_OF_USER_INVOLVED_GENERATED_GROUP_REQUEST_QUEUE_NAME)
    @Transactional
    @Override
    public void onNumberOfUserInvolvedDataGenerated(NumberOfUserInvolvedProtocolMessage protocolMessage) {
        log.info("BEFORE: onNumberOfUserInvolvedDataGenerated " + protocolMessage.toString());
        internalCommunicationService.handleNumberOfUserInvolvedData(protocolMessage);
        log.info("AFTER: onNumberOfUserInvolvedDataGenerated");
    }
}
