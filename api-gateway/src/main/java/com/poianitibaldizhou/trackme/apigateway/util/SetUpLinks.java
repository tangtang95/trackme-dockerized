package com.poianitibaldizhou.trackme.apigateway.util;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import java.util.ArrayList;
import java.util.List;

/**
 * Set up the links that will be returned to the clients in certain methods
 */
public class SetUpLinks {


    private SetUpLinks() {}

    /**
     * Get the list of links that are accessible once that the user has performed the login
     *
     * @return list of links that are accessible once that the has performed the login
     */
    public static Links getLoggedUserLinks(String serverAddress, Integer port) {
        String url = "https://"+ serverAddress +":"+ port;

        List<Link> linkList = new ArrayList<>();
        Link link1 = new Link(url+Constants.SECURED_USER_API + Constants.LOGOUT_USER_API, Constants.LOGOUT_REL);
        Link link2 = new Link(url+Constants.SECURED_USER_API + Constants.GET_USER_INFO_API, Constants.INFO_REL);
        Link link3 = new Link(url+Constants.EXT_API_GET_PENDING_REQUESTS, Constants.EXT_API_GET_PENDING_REQUESTS_REL);
        Link link4 = new Link(url+Constants.EXT_API_HEALTH_DATA, Constants.EXT_API_HEALTH_DATA_REL);
        Link link5 = new Link(url+Constants.EXT_API_POSITION_DATA, Constants.EXT_API_POSITION_DATA_REL);
        Link link6 = new Link(url+Constants.EXT_API_CLUSTER_DATA, Constants.EXT_API_CLUSTER_DATA_REL);
        Link link7 = new Link(url+Constants.EXT_API_OWN_DATA, Constants.EXT_API_OWN_DATA_REL);

        linkList.add(link1);
        linkList.add(link2);
        linkList.add(link3);
        linkList.add(link4);
        linkList.add(link5);
        linkList.add(link6);
        linkList.add(link7);

        return new Links(linkList);
    }

    /**
     * Get the list of links that are accessible once that the third party customer has performed the login
     *
     * @return list of links that are accessible once that the third party customer has performed the login
     */
    public static Links getLoggedThirdPartyLinks(String serverAddress, Integer port) {
        String url = "https://"+ serverAddress +":"+ port;

        List<Link> linkList = new ArrayList<>();
        linkList.add(new Link(url + Constants.SECURED_TP_API + Constants.LOGOUT_TP_API, Constants.LOGOUT_REL));
        linkList.add(new Link(url + Constants.SECURED_TP_API + Constants.GET_TP_INFO_API, Constants.INFO_REL));
        linkList.add(new Link(url + Constants.EXT_API_GET_GROUP_REQUESTS, Constants.EXT_API_GET_GROUP_REQUESTS_REL));
        linkList.add(new Link(url + Constants.EXT_API_ADD_GROUP_REQUEST, Constants.EXT_API_ADD_GROUP_REQUEST_REL));
        linkList.add(new Link(url + Constants.EXT_API_GET_INDIVIDUAL_REQUESTS, Constants.EXT_API_GET_INDIVIDUAL_REQUESTS_REL));
        linkList.add(new Link(url + Constants.EXT_API_ADD_INDIVIDUAL_REQUEST, Constants.EXT_API_ADD_INDIVIDUAL_REQUEST_REL));

        return new Links(linkList);
    }
}
