package com.poianitibaldizhou.trackme.sharedataservice.message.publisher;

public interface NumberOfUserInvolvedDataPublisher {

    /**
     * Send the number of user involved to the message broker (rabbit-mq)
     *  @param groupRequestId the id of the group request correlated to numberOfUserInvolved
     * @param numberOfUserInvolved the number of user involved of a specific group request
     */
    void publishNumberOfUserInvolvedData(Long groupRequestId, Integer numberOfUserInvolved);

}
