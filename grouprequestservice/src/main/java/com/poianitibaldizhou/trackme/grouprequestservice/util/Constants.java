package com.poianitibaldizhou.trackme.grouprequestservice.util;

public class Constants {

    private Constants(){}

    // log messages
    public static final String LOG_ERROR = "FATAL ERROR: InternalCommunicationService null, maybe due to the settings of active profiles";

    // Exception messages
    public static final String REQUEST_ID_NOT_FOUND = "Group request not found";
    public static final String BAD_OPERATOR_REQUEST_TYPE = "Can't match the aggregator operator with that request type";
    public static final String INVALID_OPERATION = "Invalid operation. Please, check the format of the data that have been sent";
    public static final String IMPOSSIBLE_ACCESS = "Can't access this method";

    public static final String SPACE = " ";

    // Exchange and queue messages
    public static final String GROUP_REQUEST_EXCHANGE_NAME = "trackme.grouprequest-exchange";
    public static final String GROUP_REQUEST_CREATED_SHARE_DATA_QUEUE_NAME = "trackme.grouprequest.created.share-data-queue";
    public static final String GROUP_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME = "trackme.grouprequest.accepted.share-data-queue";

    public static final String NUMBER_OF_USER_INVOLVED_EXCHANGE_NAME = "trackme.number-of-user-involved-exchange";
    public static final String NUMBER_OF_USER_INVOLVED_GENERATED_GROUP_REQUEST_QUEUE_NAME = "trackme.number-of-user.generated.group-request-queue";

    // Api path
    public static final String GROUP_REQUEST_API = "/grouprequests";
    public static final String GROUP_REQUEST_BY_ID_API = "/id/{id}";
    public static final String GROUP_REQUEST_BY_THIRD_PARTY_API = "/thirdparties";
    public static final String NEW_GROUP_REQUEST_API = "/thirdparties";

    // header
    public static final String HEADER_THIRD_PARTY_ID = "thirdPartyId";

    // External API
    public static final String EXT_API_ACCESS_GROUP_REQUEST_DATA = "/sharedataservice/dataretrieval/grouprequests";
    public static final String EXT_API_ACCESS_GROUP_REQUEST_DATA_REL = "accessData";

    public static final String FAKE_URL = "http://fakeip:9999";
}
