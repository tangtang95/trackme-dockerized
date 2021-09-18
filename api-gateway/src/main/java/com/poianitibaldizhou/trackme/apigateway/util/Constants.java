package com.poianitibaldizhou.trackme.apigateway.util;

/**
 * Constants used into the project
 */
public class Constants {

    /**
     * Private constructor
     */
    private Constants() {}

    // Exception messages
    public static final String ALREADY_PRESENT_SSN = "User with the following ssn is already registered";
    public static final String ALREADY_PRESENT_USERNAME = "User with the following username is already registered";
    public static final String USER_NOT_FOUND = "User with the following username is not registered";
    public static final String ALREADY_PRESENT_EMAIL = "Third party with the following email is already registered";
    public static final String THIRD_PARTY_NOT_FOUND = "Third party with the following email is not registered";
    public static final String INVALID_OPERATION = "Error executing the operation requested. Please check that the" +
            "format of the data provided is correct.";
    public static final String USER_BAD_CREDENTIAL = "Invalid username or password";
    public static final String THIRD_PARTY_BAD_CREDENTIAL = "Invalid email or password";
    public static final String AUTHENTICATION_FILTER_MISSING_TOKEN = "Missing authentication token";
    public static final String LOGGED_USER_NOT_FOUND = "Cannot find user with the following token ";
    public static final String ACCESS_CONTROL_EXCEPTION_USER = "Users can't access api reserved to third party customers";
    public static final String ACCESS_CONTROL_EXCEPTION_TP = "Third parties can't access api reserved to users";
    public static final String API_NOT_FOUND_EXCEPTION = "Api not found";
    public static final String ERROR_IN_RESPONSE = "Error in parsing the response";

    // log error
    public static final String LOG_ERROR = "FATAL ERROR: InternalCommunicationService null, maybe due to the settings of active profiles";

    // Exchange and queues names
    public static final String USER_EXCHANGE_NAME = "trackme.user-exchange";
    public static final String USER_CREATED_SHARE_DATA_QUEUE_NAME = "trackme.user.created.share-data-queue";
    public static final String USER_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME = "trackme.user.created.individual-request-queue";

    public static final String THIRD_PARTY_EXCHANGE_NAME = "trackme.third-party-exchange";
    public static final String THIRD_PARTY_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME = "trackme.third-party.created.individual-request-queue";

    // Utils
    public static final String SPACE = " ";

    // Headers
    public static final String EMPTY_HEADER = "";
    public static final String THIRD_PARTY_ID_HEADER_KEY = "thirdPartyId";
    public static final String USER_SSN_HEADER_KEY = "userSsn";

    public static final String PUBLIC_API = "/public/**";
    public static final String JSON_HREF_QUERY = "$..href";
    public static final String SERVER_ADDRESS = "${server.address}";
    public static final String PORT = "${server.port}";
    public static final String HTTPS_PREFIX = "https://";
    public static final String PORT_SEPARATOR = ":";
    public static final String SLASH = "/";
    public static final String FAKE_IP = "fakeip";

    public static final String UTF8_CHAR_SET = "UTF-8";

    // Api regarding third parties accounts
    public static final String PUBLIC_TP_API = "/public/thirdparties";
    public static final String REGISTER_COMPANY_TP_API = "/companies";
    public static final String REGISTER_PRIVATE_TP_API = "/privates";
    public static final String LOGIN_TP_API = "/authenticate";
    public static final String LOGIN_TP_EMAIL_API_PARAM = "email";
    public static final String LOGIN_TP_PW_API_PARAM = "password";
    public static final String SECURED_TP_API = "/thirdparties";
    public static final String GET_TP_INFO_API = "/info";
    public static final String LOGOUT_TP_API = "/logout";

    // Api regarding user accounts
    public static final String PUBLIC_USER_API = "/public/users";
    public static final String REGISTER_USER_API = "/{ssn}";
    public static final String LOGIN_USER_API = "/authenticate";
    public static final String LOGIN_USER_USERNAME_PARAM = "username";
    public static final String LOGIN_USER_PW_PARAM = "password";
    public static final String SECURED_USER_API = "/users";
    public static final String LOGOUT_USER_API = "/logout";
    public static final String GET_USER_INFO_API = "/info";

    // External APIs
    public static final String EXT_API_GET_PENDING_REQUESTS = "/individualrequestservice/requests/users";
    public static final String EXT_API_HEALTH_DATA = "/sharedataservice/datacollection/healthdata";
    public static final String EXT_API_POSITION_DATA = "/sharedataservice/datacollection/positiondata";
    public static final String EXT_API_CLUSTER_DATA = "/sharedataservice/datacollection/clusterdata";
    public static final String EXT_API_OWN_DATA = "/sharedataservice/dataretrieval/users";

    public static final String EXT_API_GET_GROUP_REQUESTS = "/grouprequestservice/grouprequests/thirdparties";
    public static final String EXT_API_ADD_INDIVIDUAL_REQUEST = "/individualrequestservice/requests";
    public static final String EXT_API_GET_INDIVIDUAL_REQUESTS = "/individualrequestservice/requests/thirdparties";
    public static final String EXT_API_ADD_GROUP_REQUEST = "/grouprequestservice/grouprequests/thirdparties";


    // External APIs rel
    public static final String EXT_API_GET_PENDING_REQUESTS_REL = "pendingRequests";
    public static final String EXT_API_HEALTH_DATA_REL = "postHealthData";
    public static final String EXT_API_POSITION_DATA_REL = "postPositionData";
    public static final String EXT_API_CLUSTER_DATA_REL = "postClusterData";
    public static final String EXT_API_OWN_DATA_REL = "getOwnData";

    public static final String EXT_API_GET_GROUP_REQUESTS_REL = "groupRequests";
    public static final String EXT_API_ADD_GROUP_REQUEST_REL = "newGroupRequest";
    public static final String EXT_API_GET_INDIVIDUAL_REQUESTS_REL = "individualRequests";
    public static final String EXT_API_ADD_INDIVIDUAL_REQUEST_REL = "newIndividualRequest";

    // Internal APIs rel
    public static final String LOGOUT_REL = "logout";
    public static final String INFO_REL = "info";

}

