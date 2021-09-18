package com.trackme.trackmeapplication.httpConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * Builder class for building the http message
 */
public class ConnectionBuilder {

    private ConnectionThread connectionThread;

    /**
     * Constructor.
     *
     * @param lockInterface interface for handing the synchronization between the thread.
     */
    public ConnectionBuilder(LockInterface lockInterface) {
        SSL ssl = SSL.getInstance();
        connectionThread = new ConnectionThread(ssl.getSslContext(), ssl.getHostnameVerifier(), lockInterface);
    }

    /**
     * Set the url in the message
     *
     * @param url the url to set
     * @return connectionBuilder
     */
    public ConnectionBuilder setUrl(String url) {
        connectionThread.setUrl(url);
        return this;
    }

    /**
     * Set the method in the message
     *
     * @param method the method to set(POST, GET)
     * @return connectionBuilder
     */
    public ConnectionBuilder setHttpMethod(HttpMethod method) {
        connectionThread.setHttpAction(method);
        return this;
    }

    /**
     * Set the entity in the message
     *
     * @param entity the entity to set(header + body)
     * @return connectionBuilder
     */
    public ConnectionBuilder setEntity(HttpEntity entity) {
        connectionThread.setEntity(entity);
        return this;
    }

    /**
     * Getter method.
     *
     * @return the connection thread with the message set.
     */
    public ConnectionThread getConnection() {
        return connectionThread;
    }

}
