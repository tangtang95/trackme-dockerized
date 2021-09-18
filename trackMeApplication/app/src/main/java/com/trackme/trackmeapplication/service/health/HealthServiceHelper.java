package com.trackme.trackmeapplication.service.health;

import android.app.Service;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.exception.NoPermissionException;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface HealthServiceHelper {

    /**
     * @return the service application of SOS
     */
    Service getService();

    /**
     * Retrieves the birth date of the user of the service
     *
     * @return the java.util.Date of the user
     */
    Date getUserBirthDate();

    /**
     * Set the user birth date
     * @param birthDate the birth date of the user
     */
    void setUserBirthDate(Date birthDate);

    /**
     * Save the health data in the local DB
     *
     * @param healthData the new health data
     */
    void saveHealthData(HealthData healthData);

    /**
     * @return true if there are recent (within 1 hour) emergency call, false otherwise
     * @throws InterruptedException when the thread executing the retrieves of the recent call is interrupted
     * @throws ExecutionException when the thread execution has some issue
     * @throws TimeoutException when the timeout waiting for the thread has elapsed
     */
    boolean hasRecentEmergencyCall() throws InterruptedException, ExecutionException, TimeoutException;

    /**
     * Make an emergency call to the number given by the HealthServiceHelper
     *
     * @return true if successful, false otherwise
     * @throws InterruptedException when the thread executing the insertion of the new call is interrupted
     * @throws TimeoutException when the timeout waiting for the thread has elapsed
     * @throws NoPermissionException when the permission to make the call are not granted
     * @throws EmergencyNumberNotFoundException when there is no emergency number available in the actual country
     */
    boolean makeEmergencyCall() throws InterruptedException, TimeoutException,
            NoPermissionException, EmergencyNumberNotFoundException;
}
