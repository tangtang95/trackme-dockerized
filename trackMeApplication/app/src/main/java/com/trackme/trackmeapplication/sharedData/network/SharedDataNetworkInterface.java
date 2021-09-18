package com.trackme.trackmeapplication.sharedData.network;

import com.trackme.trackmeapplication.home.userHome.HistoryItem;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.ClusterDataWrapper;
import com.trackme.trackmeapplication.sharedData.HealthDataWrapper;
import com.trackme.trackmeapplication.sharedData.PositionDataWrapper;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface that expose the main functions to communicate to the shareDataService on the server
 * (Controller)
 *
 * @author Mattia Tibaldi
 */
public interface SharedDataNetworkInterface {

    /**
     * Getter method.
     *
     * @param token user token
     * @return all the user info
     * @throws UserNotFoundException throw when the user is not found on server
     * @throws ConnectionException throw when an error in the connection is performed
     */
    User getUser(String token) throws UserNotFoundException, ConnectionException;

    /**
     * Getter method
     *
     * @param token third party token
     * @return all the third party info
     * @throws UserNotFoundException throw when the user is not found on server
     * @throws ConnectionException throw when an error in the connection is performed
     */
    ThirdPartyInterface getThirdParty(String token) throws UserNotFoundException, ConnectionException;

    /**
     * Getter method
     *
     * @param token third party token
     * @param url url to the object
     * @return the user data required in the group request
     * @throws ConnectionException throw when an error in the connection is performed
     */
    String getGroupRequestData(String token, String url) throws ConnectionException;

    /**
     * Getter method
     *
     * @param token third party token
     * @param url url to the object
     * @return the user data required in the individual request
     * @throws ConnectionException throw when an error in the connection is performed
     */
    String getIndividualRequestData(String token, String url) throws ConnectionException;

    /**
     * Getter method
     *
     * @param token user token
     * @param startDate initial data of the request
     * @param endDate final date of the request
     * @return the user data saved on the server between the two date specified
     * @throws ConnectionException throw when an error in the connection is performed
     */
    List<HistoryItem> getUserData(String token, String startDate, String endDate) throws ConnectionException;

    /**
     * Send health data to the server
     *
     * @param token user token
     * @param healthData health data to send
     * @throws ConnectionException throw when an error in the connection is performed
     */
    void sendHealthData(String token, HealthDataWrapper healthData) throws ConnectionException;

    /**
     * Send position data to the server
     *
     * @param token user token
     * @param positionData position data to send
     * @throws ConnectionException throw when an error in the connection is performed
     */
    void sendPositionData(String token, PositionDataWrapper positionData) throws ConnectionException;

    /**
     * Send cluster data (position + health status) to the server
     *
     * @param token user token
     * @param clusterData cluster data to send
     * @throws ConnectionException throw when an error in the connection is performed
     */
    void sendClusterData(String token, ClusterDataWrapper clusterData) throws ConnectionException;

}
