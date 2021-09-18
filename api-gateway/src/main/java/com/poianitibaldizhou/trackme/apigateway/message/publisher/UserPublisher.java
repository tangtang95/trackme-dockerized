package com.poianitibaldizhou.trackme.apigateway.message.publisher;

import com.poianitibaldizhou.trackme.apigateway.entity.User;

public interface UserPublisher {

    /**
     * Send the user created to the message broker (rabbit-mq)
     * @param user the user created to be sent
     */
    void publishUserCreated(User user);

}
