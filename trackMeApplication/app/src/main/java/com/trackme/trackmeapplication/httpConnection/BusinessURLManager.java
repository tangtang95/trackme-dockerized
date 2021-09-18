package com.trackme.trackmeapplication.httpConnection;

import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.baseUtility.Constant;

import java.util.List;
import java.util.Map;

/**
 * Static context that allows to manage the url received from the server for all the possible
 * action that the third party can perform.
 *
 * @author Mattia Tibaldi
 */
public class BusinessURLManager {

    private static BusinessURLManager instance;

    private Map<String,Map<String, String>> map = null;

    /**
     * Private constructor
     */
    private BusinessURLManager(){
    }

    /**
     * @return the current instance if exist otherwise it instantiates new one
     */
    public static BusinessURLManager getInstance(){
        if (instance == null)
            instance = new BusinessURLManager();
        return instance;
    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accessing the resource identified by the uri
     */
    public String createURLWithPort(String uri) {
        return "https://"+ Settings.getServerAddress() + ":" + Settings.getServerPort() + uri;
    }

    /**
     * Save the url revived in a Map
     *
     * @param urls list of urls
     */
    public void setUrls(List<String> urls) {
        //Log.d("URLS", urls.toString());
        List<Map<String, Map<String, String>>> mapList =  JsonPath.read(urls.toString(), "$");
        map = mapList.get(0);
    }

    /**
     * Getter method.
     *
     * @return the logout link.
     */
    public String getLogoutLink() {
        if (map == null)
            return createURLWithPort("/thirdparties/logout");
        return map.get(Constant.MAP_LOGOUT_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the info link.
     */
    public String getUserInfoLink() {
        return map.get(Constant.MAP_USER_INFO_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the group request link.
     */
    public String getGroupRequestsLink() {
        return map.get(Constant.MAP_GROUP_REQUESTS_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the new group request link.
     */
    public String getNewGroupRequestLink() {
        return map.get(Constant.MAP_NEW_GROUP_REQUEST_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the individual request link.
     */
    public String getIndividualRequestsLink() {
        return map.get(Constant.MAP_INDIVIDUAL_REQUESTS_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the new individual request link.
     */
    public String getNewIndividualRequestLink() {
        return map.get(Constant.MAP_NEW_INDIVIDUAL_REQUEST_KEY).get(Constant.MAP_HREF_KEY);
    }
}
