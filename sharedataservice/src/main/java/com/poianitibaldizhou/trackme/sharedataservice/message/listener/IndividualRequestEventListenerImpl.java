package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class IndividualRequestEventListenerImpl implements IndividualRequestEventListener {

    private final InternalCommunicationService internalCommunicationService;

    public IndividualRequestEventListenerImpl(InternalCommunicationService internalCommunicationService) {
        this.internalCommunicationService = internalCommunicationService;
    }

    @RabbitListener(queues = Constants.INDIVIDUAL_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onIndividualRequestAccepted(IndividualRequestProtocolMessage individualRequestProtocol) {
        log.info("BEFORE: onIndividualRequestAccepted " + individualRequestProtocol.toString());
        if(!IndividualRequestProtocolMessage.validateMessage(individualRequestProtocol)){
            log.error("FATAL ERROR: Received am individual request which has not all attributes non-null "
                    + individualRequestProtocol);
            return;
        }
        if(!individualRequestProtocol.getStatus().equals(IndividualRequestStatusProtocolMessage.ACCEPTED)) {
            log.error("FATAL ERROR: Received an individual request which is not ACCEPTED" + individualRequestProtocol);
            return;
        }
        internalCommunicationService.handleIndividualRequestAccepted(individualRequestProtocol);
        log.info("AFTER: onIndividualRequestAccepted");
    }
}
