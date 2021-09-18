package com.trackme.trackmeapplication.httpConnection;

import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.baseUtility.Constant;

import java.util.List;
import java.util.Map;

/**
 * Static context that allows to manage the url received from the server for all the possible
 * action that the user can perform.
 *
 * @author Mattia Tibaldi
 */
public class UserURLManager {

    private static UserURLManager instance;

    private Map<String,Map<String, String>> map = null;

    /**
     * Private constructor
     */
    private UserURLManager(){
    }

    /**
     * @return the current instance if exist otherwise it instantiates new one
     */
    public static UserURLManager getInstance(){
        if (instance == null)
            instance = new UserURLManager();
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
            return createURLWithPort("/users/logout");
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
     * @return the pending request link.
     */
    public String getPendingRequestsLink() {
        return map.get(Constant.MAP_PENDING_REQUEST_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the post heath data link.
     */
    public String getPostHealthDataLink() {
        return map.get(Constant.MAP_POST_HEALTH_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the post position data link.
     */
    public String getPostPositionDataLink() {
        return map.get(Constant.MAP_POST_POSITION_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the post cluster data link.
     */
    public String getPostClusterDataLink() {
        return map.get(Constant.MAP_CLUSTER_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }

    /**
     * Getter method.
     *
     * @return the own data link.
     */
    public String getGetOwnDataLink() {
        return map.get(Constant.MAP_OWN_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }
}
