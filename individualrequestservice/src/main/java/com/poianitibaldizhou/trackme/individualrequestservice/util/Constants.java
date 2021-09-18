package com.poianitibaldizhou.trackme.individualrequestservice.util;

/**
 * Constants string extrapolated in this class
 */
public class Constants {

    /**
     * Private constructor for hiding the public one
     */
    private Constants(){

    }
    // HATEOAS rel
    public static final String REL_USER_PENDING_REQUEST = "userPendingRequest";
    public static final String REL_REQUEST = "request";
    public static final String REL_ADD_RESPONSE = "addResponse";
    public static final String REL_BLOCK_TP = "blockThirdParty";

    // Constants that regards exception messages
    public static final String INVALID_CREDENTIAL = "Could not access this data";
    public static final String THIRD_PARTY_NOT_FOUND = "Could not find third party";
    public static final String REQUEST_ID_NOT_FOUND = "Could not find request";
    public static final String USER_NOT_FOUND = "Could not find user";
    public static final String RESPONSE_ALREADY_PRESENT = "Response already present for the request";
    public static final String NON_MATCHING_USER = "The following ssn is not the one related with the request:";
    public static final String TP_ALREADY_BLOCKED = "Third party customer already blocked";
    public static final String BAD_RESPONSE_TYPE = "Could not find response type";
    public static final String REFUSED_REQUEST_NOT_FOUND = "No refused request has been found of the following" +
            "third party customer";
    public static final String INCOMPATIBLE_DATE = "Start date must be before the end date";
    public static final String INVALID_OPERATION = "Error executing the operation requested. Please check that the " +
            "format of the data provided is correct.";

    public static final String SPACE = " ";

    // Exchange and queue names
    public static final String USER_EXCHANGE_NAME = "trackme.user-exchange";
    public static final String USER_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME = "trackme.user.created.individual-request-queue";

    public static final String THIRD_PARTY_EXCHANGE_NAME = "trackme.third-party-exchange";
    public static final String THIRD_PARTY_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME = "trackme.third-party.created.individual-request-queue";

    public static final String INDIVIDUAL_REQUEST_EXCHANGE_NAME = "trackme.individualrequest-exchange";
    public static final String INDIVIDUAL_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME = "trackme.individualrequest.accepted.share-data-queue";

    // Api path for requests
    public static final String REQUEST_API = "/requests";
    public static final String REQUEST_BY_ID_API = "/id/{id}";
    public static final String PENDING_REQUEST_BY_USER_API = "/users";
    public static final String REQUEST_BY_THIRD_PARTY_ID = "/thirdparties";
    public static final String NEW_REQUEST_API = "/{ssn}";

    // Api path for responses
    public static final String RESPONSE_API = "/responses";
    public static final String NEW_RESPONSE_API = "/requests/{requestID}";
    public static final String NEW_BLOCK_API = "/blockedThirdParty/thirdparties/{thirdParty}";

    // Header parameter in api
    public static final String HEADER_THIRD_PARTY_ID = "thirdPartyId";
    public static final String HEADER_USER_SSN = "userSsn";

    // External Api
    public static final String EXT_API_ACCESS_INDIVIDUAL_REQUEST_DATA = "/sharedataservice/dataretrieval/individualrequests";
    public static final String EXTP_API_ACCESS_INDIVIDUAL_REQUEST_DATA_REL = "accessData";
    public static final String FAKE_URL = "http://fakeip:9999";
}
