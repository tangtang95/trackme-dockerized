package com.poianitibaldizhou.trackme.grouprequestservice.message.publisher;

import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;

public interface GroupRequestPublisher {

    /**
     * Send the group request wrapper to the message broker (rabbit-mq)
     *
     * @param groupRequestWrapper the group request wrapper containing also the list of filter statements
     */
    void publishGroupRequest(GroupRequestWrapper groupRequestWrapper);

}
