package com.trackme.trackmeapplication.service.health;

import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.exception.InvalidHealthDataException;
import com.trackme.trackmeapplication.service.exception.NoPermissionException;
import com.trackme.trackmeapplication.service.util.HealthDataInspector;
import com.trackme.trackmeapplication.service.util.HealthDataInspectorImpl;
import com.trackme.trackmeapplication.service.util.MessageType;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class HealthDataCallback implements Callback {

    private HealthServiceHelper helper;

    /**
     * Constructor.
     * Create a health data callback to handle message from the bluetooth socket
     *
     * @param helper the SOS helper running in notification foreground
     */
    public HealthDataCallback(HealthServiceHelper helper) {
        this.helper = helper;
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MessageType.HEALTH_DATA:
                boolean success = false;
                HealthData healthData = (HealthData) message.obj;

                // Save health data if it is a valid one
                if (healthData.isValidData())
                    helper.saveHealthData(healthData);

                // Make calls if necessary
                HealthDataInspector healthDataInspector = new HealthDataInspectorImpl(helper.getUserBirthDate());
                try {
                    if (healthDataInspector.isGraveCondition(healthData)) {
                        if (!helper.hasRecentEmergencyCall()) {
                            success = helper.makeEmergencyCall();
                        }
                    }
                } catch (InvalidHealthDataException e) {
                    Log.e(helper.getService().getString(R.string.debug_tag), helper.getService().getString(R.string.make_call_error_invalid_data));
                } catch (EmergencyNumberNotFoundException e) {
                    Log.e(helper.getService().getString(R.string.debug_tag), helper.getService().getString(R.string.make_call_error_no_number));
                } catch (NoPermissionException e) {
                    Log.e(helper.getService().getString(R.string.debug_tag), helper.getService().getString(R.string.make_call_error_no_permission));
                } catch (InterruptedException|ExecutionException|TimeoutException e) {
                    Log.e(helper.getService().getString(R.string.debug_tag), helper.getService().getString(R.string.make_call_error_due_to_thread));
                }
                return success;
            default:
                break;
        }

        return false;
    }
}
