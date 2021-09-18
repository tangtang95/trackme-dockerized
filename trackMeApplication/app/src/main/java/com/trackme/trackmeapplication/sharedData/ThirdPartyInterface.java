package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

/**
 * Interface that implement the strategy pattern for the third party user (privateUser or Company)
 *
 * @author Mattia Tibaldi
 */
public interface ThirdPartyInterface extends Serializable {

    /**
     * Getter method
     *
     * @return return the name of the third party
     */
    String extractName();

    /**
     * Getter method
     *
     * @return the mail of the third party
     */
    String extractEmail();

    /**
     * Getter method
     *
     * @return the password of the third party
     */
    String extractPassword();

}
