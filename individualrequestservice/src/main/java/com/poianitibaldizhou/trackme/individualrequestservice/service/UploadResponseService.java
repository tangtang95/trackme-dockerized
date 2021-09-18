package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.Response;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;

/**
 * Interface provided to offer the service that manages that upload of responses to request
 */
public interface UploadResponseService {

    /**
     * Upload a response to a request that a certain user has received.
     *
     * Note that a request from a third party has to be performed to the user identified by ssn.
     * The request must not have already a response associated.
     *
     *
     * @param requestID identified of the request
     * @param response response to the request
     * @param user user providing the response: this has to be an existing user
     * @return uploaded response
     */
    Response addResponse(Long requestID, ResponseType response, User user);


    /**
     * User identified by the ssn blocks the third party customer identified by thirdParty.
     * The system will refuse all the request of that customer towards the user.
     * It is necessary that the user exists and that the third party has performed at least a request, that
     * has been refused, on that user.
     *
     * @param user user that performs the block
     * @param thirdPartyID third party that will be blocked
     * @return information regarding the block
     */
    BlockedThirdParty addBlock(User user, Long thirdPartyID);
}
