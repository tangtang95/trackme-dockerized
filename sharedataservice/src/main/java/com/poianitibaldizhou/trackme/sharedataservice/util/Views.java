package com.poianitibaldizhou.trackme.sharedataservice.util;

/**
 * Json views by Jackson
 */
public class Views {
    private Views(){}

    /**
     * Public views that can be seen by the user or third party
     */
    public static class Public {
    }

    /**
     * Internal views that can be seen only by developers
     */
    public static class Internal extends Public {
    }

}
