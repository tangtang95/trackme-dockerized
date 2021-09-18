package com.trackme.trackmeapplication.baseUtility;

/**
 * Class whit all the constant string value in the project. It allows to a easily refactor of the
 * messages and a better maintainability of the code.
 *
 * @author Mattia Tibaldi
 *
 */
public final class Constant {

    /*String patterns*/
    public static final String SSN_PATTERN = "^\\d{3}-\\d{2}-\\d{4}$";
    public static final String E_MAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String REQUEST_FOLDER_NAME = "Request files";

    public static final String KEY_STORE_TYPE = "PKCS12";
    public static final char[] password = {'b', 'i', 'c', 'm', 'o', 'u', 's', 'e', 'p', 'e', 'n', 'c', 'i', 'l'};

    public static final String FINISH_ACTION = "finish_activity";

    //String used in shared preference
    public static final String LOGIN_SHARED_DATA_NAME = "login_data";
    public static final String SD_BUSINESS_TOKEN_KEY = "business_token";
    public static final String SD_USER_TOKEN_KEY = "user_token";
    public static final String SD_SERVER_ADDRESS_KEY = "server_address";
    public static final String SD_INDIVIDUAL_REQUEST_KEY = "individual_request_item";
    public static final String SD_GROUP_REQUEST_KEY = "group_request_item";
    public static final String USER_LOGGED_BOOLEAN_VALUE_KEY = "user_logged";
    public static final String BUSINESS_LOGGED_BOOLEAN_VALUE_KEY = "business_logged";
    public static final String BIRTH_DATE_KEY = "birth_date";

    // Api regarding third parties accounts
    public static final String PUBLIC_TP_API = "/public/thirdparties";
    public static final String REGISTER_COMPANY_TP_API = "/companies";
    public static final String REGISTER_PRIVATE_TP_API = "/privates";

    // Api regarding user accounts
    public static final String PUBLIC_USER_API = "/public/users";
    public static final String REGISTER_USER_API = "/";
    public static final String LOGIN_USER_API = "/authenticate";

    //URL Map key
    public static final String MAP_LOGOUT_KEY = "logout";
    public static final String MAP_USER_INFO_KEY = "info";
    public static final String MAP_PENDING_REQUEST_KEY = "pendingRequests";
    public static final String MAP_POST_HEALTH_DATA_KEY = "postHealthData";
    public static final String MAP_POST_POSITION_DATA_KEY = "postPositionData";
    public static final String MAP_CLUSTER_DATA_KEY = "postClusterData";
    public static final String MAP_OWN_DATA_KEY = "getOwnData";
    public static final String MAP_HREF_KEY = "href";
    public static final String MAP_GROUP_REQUESTS_KEY = "groupRequests";
    public static final String MAP_NEW_GROUP_REQUEST_KEY = "newGroupRequest";
    public static final String MAP_INDIVIDUAL_REQUESTS_KEY = "individualRequests";
    public static final String MAP_NEW_INDIVIDUAL_REQUEST_KEY = "newIndividualRequest";

    //Group request map key
    public static final String MAP_COLUMN_KEY = "column";
    public static final String MAP_COMPARISON_SYMBOL_KEY = "comparisonSymbol";
    public static final String MAP_VALUE_KEY = "value";

}
