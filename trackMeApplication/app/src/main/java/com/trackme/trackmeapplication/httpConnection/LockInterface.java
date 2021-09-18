package com.trackme.trackmeapplication.httpConnection;

/**
 * lOck interface that exposes the ain function for managing the lock between the controller and tha
 * connectionThread
 *
 * @author Mattia Tibaldi
 */
public interface LockInterface {

    /**
     * Getter method.
     *
     * @return the object locked
     */
    Object getLock();
}
