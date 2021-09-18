package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class UserEventListenerImpl implements UserEventListener {

    private final InternalCommunicationService internalCommunicationService;

    public UserEventListenerImpl(InternalCommunicationService internalCommunicationService) {
        this.internalCommunicationService = internalCommunicationService;
    }

    @RabbitListener(queues = Constants.USER_CREATED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onUserCreated(@Payload UserProtocolMessage userProtocol) {
        log.info("BEFORE: onUserCreated " + userProtocol.toString());
        if(!UserProtocolMessage.validateMessage(userProtocol)){
            log.error("FATAL ERROR: Received a user which has not all attributes non-null " + userProtocol);
            return;
        }
        internalCommunicationService.handleUserCreated(userProtocol);
        log.info("AFTER: onUserCreated");
    }
}
