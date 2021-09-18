package com.trackme.trackmeapplication.httpConnection;

/**
 * Application setting
 *
 * @author Mattia Tibaldi
 */
public class Settings {

    private static int serverPort = 8443;
    private static String serverAddress = "192.168.1.196";
    private static int refreshItemTime = 10000;
    private static int pushDataTime = 120000;

    //getter methods
    public static int getServerPort() {
        return serverPort;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getRefreshItemTime() {
        return refreshItemTime;
    }

    public static int getPushDataTime() {
        return pushDataTime;
    }

    //setter methods
    public static void setServerPort(int serverPort) {
        Settings.serverPort = serverPort;
    }

    public static void setServerAddress(String serverAddress) {
        Settings.serverAddress = serverAddress;
    }

    public static void setRefreshItemTime(int refreshItemTime) {
        Settings.refreshItemTime = refreshItemTime;
    }
}
