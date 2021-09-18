package com.poianitibaldizhou.trackme.individualrequestservice.message.publisher;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;

public interface IndividualRequestPublisher {

    /**
     * Send the individual request to the message broker (rabbit-mq)
     *
     * @param individualRequest the individual request to be sent
     */
    void publishIndividualRequest(IndividualRequest individualRequest);

}
