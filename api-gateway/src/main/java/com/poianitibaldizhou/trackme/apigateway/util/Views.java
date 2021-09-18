package com.poianitibaldizhou.trackme.apigateway.util;

/**
 * Views useful to set the fields of an entity that are used in a certain controller method
 */
public class Views {
    private Views(){}

    /**
     * Public views: used in the more general case
     */
    public static class Public{}

    /**
     * Secured view that contains sensible information, such as passwords
     */
    public static class Secured extends Public{}
}
