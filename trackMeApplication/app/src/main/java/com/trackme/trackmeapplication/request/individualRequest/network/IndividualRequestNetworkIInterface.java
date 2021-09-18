package com.trackme.trackmeapplication.request.individualRequest.network;

import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.exception.ThirdPartyBlockedException;
import com.trackme.trackmeapplication.request.individualRequest.IndividualRequest;
import com.trackme.trackmeapplication.request.individualRequest.IndividualRequestWrapper;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface that exposes all the functions useful for the communication between client and the
 * individualRequestService on the server.
 * (Controller)
 *
 * @author Mattia Tibaldi
 */
public interface IndividualRequestNetworkIInterface {

    /**
     * Getter method.
     *
     * @param token third party token
     * @return a list with all individual request sent
     * @throws ConnectionException throw when an connection error is performed
     */
    List<IndividualRequestWrapper> getIndividualRequest(String token) throws ConnectionException;

    /**
     * Getter method
     *
     * @param token user token
     * @return a list with all the pending individual request
     * @throws ConnectionException throw when an connection error is performed
     */
    List<IndividualRequestWrapper> getOwnIndividualRequest(String token) throws ConnectionException;

    /**
     * Accept a request sent by the third party
     *
     * @param token user token
     * @param url self url to the resource
     * @throws ConnectionException throw when an connection error is performed
     */
    void acceptIndividualRequest(String token, String url) throws ConnectionException;

    /**
     * Refuse a request sent by the third party
     *
     * @param token user token
     * @param url self url to the resource
     * @return a url to block the third party
     * @throws ConnectionException throw when an connection error is performed
     */
    String refuseIndividualRequest(String token, String url) throws ConnectionException;

    /**
     * Block a third party user.
     *
     * @param token user token
     * @param url self url to the resource
     * @throws ConnectionException throw when an connection error is performed
     */
    void blockThirdPartyCustomer(String token, String url) throws ConnectionException;

    /**
     * Send a new individual request to the user
     *
     * @param token third party token
     * @param individualRequest individual request to send
     * @param userSSN ssn of the user
     * @throws ConnectionException throw when an connection error is performed
     * @throws RequestNotWellFormedException throw when a request is not well formed
     * @throws ThirdPartyBlockedException Throw when a third party blocked trie to send a request
     */
    void send(String token, IndividualRequest individualRequest, String userSSN) throws ConnectionException, RequestNotWellFormedException, ThirdPartyBlockedException, UserNotFoundException;
}
