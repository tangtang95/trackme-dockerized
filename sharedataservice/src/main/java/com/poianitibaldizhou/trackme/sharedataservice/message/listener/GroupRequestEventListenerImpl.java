package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.GroupRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class GroupRequestEventListenerImpl implements GroupRequestEventListener {

    private final InternalCommunicationService internalCommunicationService;

    public GroupRequestEventListenerImpl(InternalCommunicationService internalCommunicationService) {
        this.internalCommunicationService = internalCommunicationService;
    }

    @RabbitListener(queues = Constants.GROUP_REQUEST_CREATED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onGroupRequestCreated(@Payload GroupRequestProtocolMessage groupRequestProtocol) {
        log.info("BEFORE: onGroupRequestCreated " + groupRequestProtocol.toString());
        if (!GroupRequestProtocolMessage.validateMessage(groupRequestProtocol)) {
            log.error("FATAL ERROR: Received a group request which has not all attributes non-null " + groupRequestProtocol);
            return;
        }
        if (!groupRequestProtocol.getStatus().equals(GroupRequestStatusProtocolMessage.UNDER_ANALYSIS)) {
            log.error("FATAL ERROR: Received a group request which is not UNDER_ANALYSIS " + groupRequestProtocol);
            return;
        }
        internalCommunicationService.handleGroupRequestCreated(groupRequestProtocol);
        log.info("AFTER: onGroupRequestCreated");
    }

    @RabbitListener(queues = Constants.GROUP_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onGroupRequestAccepted(@Payload GroupRequestProtocolMessage groupRequestProtocol) {
        log.info("BEFORE: onGroupRequestAccepted " + groupRequestProtocol.toString());
        if (!GroupRequestProtocolMessage.validateMessage(groupRequestProtocol)) {
            log.error("FATAL ERROR: Received a group request which has not all attributes non-null " +
                    groupRequestProtocol);
            return;
        }
        if (!groupRequestProtocol.getStatus().equals(GroupRequestStatusProtocolMessage.ACCEPTED)) {
            log.error("FATAL ERROR: Received a group request which is not ACCEPTED " + groupRequestProtocol);
            return;
        }

        internalCommunicationService.handleGroupRequestAccepted(groupRequestProtocol);
        log.info("AFTER: onGroupRequestAccepted");
    }
}
