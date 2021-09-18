package com.poianitibaldizhou.trackme.individualrequestservice.message.listener;

import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.ThirdPartyProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class ThirdPartyEventListenerImpl implements ThirdPartyEventListener {

    private InternalCommunicationService internalCommunicationService;

    public ThirdPartyEventListenerImpl(InternalCommunicationService internalCommunicationService) {
        this.internalCommunicationService = internalCommunicationService;
    }

    @RabbitListener(queues = Constants.THIRD_PARTY_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME)
    @Transactional
    @Override
    public void onThirdPartyCreated(ThirdPartyProtocolMessage protocolMessage) {
        if(ThirdPartyProtocolMessage.validatePrivateDetailMessage(protocolMessage) &&
                ThirdPartyProtocolMessage.validateCompanyDetailMessage(protocolMessage)){
            log.error("FATAL ERROR: Unexpected third party protocol message with all fields non null");
            return;
        }

        if(ThirdPartyProtocolMessage.validateCompanyDetailMessage(protocolMessage)) {
            internalCommunicationService.handleCompanyThirdParty(protocolMessage);
        }
        else if(ThirdPartyProtocolMessage.validatePrivateDetailMessage(protocolMessage)) {
            internalCommunicationService.handlePrivateThirdParty(protocolMessage);
        }
        else{
            log.error("FATAL ERROR: Unexpected third party protocol message with null parameters in both private and company");
        }
    }
}
