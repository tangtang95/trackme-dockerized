package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception thrown when an user wants to block a third party he had already blocked
 */
public class BlockAlreadyPerformedException extends RuntimeException {

    /**
     * Creates a new block already performed exception
     *
     * @param thirdPartyID the user is trying to blocking twice the third party customer identified by thirdParty
     */
    public BlockAlreadyPerformedException(Long thirdPartyID) {
        super(Constants.TP_ALREADY_BLOCKED + Constants.SPACE + thirdPartyID);
    }
}
