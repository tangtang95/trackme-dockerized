package com.trackme.trackmeapplication.request.groupRequest.network;

import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.exception.ThirdPartyBlockedException;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequestBuilder;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequestWrapper;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface that exposes the main function for communicate with the groupRequestService on the server
 * (controller)
 *
 * @author Mattia Tibaldi
 */
public interface GroupRequestNetworkInterface {

    /**
     * Return all the possible aggregator function
     *
     * @return a list of aggregator functions
     */
    List<String> getAggregators();

    /**
     * Return all the possible request types
     *
     * @return a list of request types
     */
    List<String> getRequestTypes();

    /**
     * Return all the possible operators useful.
     *
     * @return a list of operators.
     */
    List<String> getOperators();

    /**
     * Return all the columns where is possible add a filter
     *
     * @return a list of columns
     */
    List<String> getDbColumns();

    /**
     * Getter method
     *
     * @param token third party token
     * @return a list of group request
     * @throws ConnectionException throw when a connection error is performed
     */
    List<GroupRequestWrapper> getGroupRequest(String token) throws ConnectionException;

    /**
     * Send a new group request
     *
     * @param token third party token
     * @param groupRequestBuilder the builder of the group request
     * @throws ConnectionException throw when a connection error is performed
     * @throws RequestNotWellFormedException throw when the group request is not well formed
     * @throws ThirdPartyBlockedException Throw when a third party blocked trie to send a request
     */
    void send(String token, GroupRequestBuilder groupRequestBuilder) throws ConnectionException, RequestNotWellFormedException, ThirdPartyBlockedException, UserNotFoundException;

}
