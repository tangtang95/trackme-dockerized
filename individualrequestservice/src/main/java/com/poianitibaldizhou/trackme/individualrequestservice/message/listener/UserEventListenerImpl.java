package com.poianitibaldizhou.trackme.individualrequestservice.message.listener;

import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class UserEventListenerImpl implements UserEventListener {

    private final InternalCommunicationService internalCommunicationService;

    public UserEventListenerImpl(InternalCommunicationService internalCommunicationService) {
        this.internalCommunicationService = internalCommunicationService;
    }

    @RabbitListener(queues = Constants.USER_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME)
    @Transactional
    @Override
    public void onUserCreated(UserProtocolMessage userProtocolMessage) {
        log.info("BEFORE: onUserCreated " + userProtocolMessage.toString());
        internalCommunicationService.handleUserCreated(userProtocolMessage);
        log.info("AFTER: onUserCreated");
    }
}
