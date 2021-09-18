package com.trackme.trackmeapplication.service.health;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.util.MessageType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class HealthDataCallbackTest {

    @Mock
    private HealthServiceHelper helper;

    private HealthService androidService;

    private HealthDataCallback healthDataCallback;

    @Rule
    public ServiceTestRule mServiceTestRule = new ServiceTestRule();

    @Before
    public void setUp() throws Exception {
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), HealthService.class);
        IBinder binder = mServiceTestRule.bindService(serviceIntent);
        androidService = ((HealthService.LocalBinder)binder).getService();
        helper = mock(HealthServiceHelper.class);
        setUpHelper();
    }

    public void setUpHelper() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(1990, 10, 10);

        when(helper.getService()).thenReturn(androidService);
        when(helper.getUserBirthDate()).thenReturn(calendar.getTime());

        healthDataCallback = new HealthDataCallback(helper);
    }

    private HealthData getGraveHealthData(){
        HealthData healthData = new HealthData();
        healthData.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        healthData.setHeartbeat(22);
        healthData.setPressureMin(60);
        healthData.setPressureMax(100);
        healthData.setBloodOxygenLevel(95);
        return healthData;
    }

    private HealthData getGoodHealthData(){
        HealthData healthData = new HealthData();
        healthData.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        healthData.setHeartbeat(60);
        healthData.setPressureMin(60);
        healthData.setPressureMax(100);
        healthData.setBloodOxygenLevel(95);
        return healthData;
    }

    private HealthData getInvalidHealthData(){
        HealthData healthData = new HealthData();
        healthData.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        healthData.setHeartbeat(60);
        healthData.setPressureMin(null);
        healthData.setPressureMax(100);
        healthData.setBloodOxygenLevel(95);
        return healthData;
    }

    @After
    public void tearDown() throws Exception {
        helper = null;
    }

    @Test
    public void handleMessageCorrectWithSuccessCall() throws Exception {
        when(helper.hasRecentEmergencyCall()).thenReturn(false);
        when(helper.makeEmergencyCall()).thenReturn(true);

        HealthData healthData = getGraveHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertTrue(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(1)).hasRecentEmergencyCall();
        verify(helper, times(1)).makeEmergencyCall();
    }

    @Test
    public void handleMessageCorrectWithRecentEmergencyCall() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(true);
        when(helper.makeEmergencyCall()).thenReturn(true);

        HealthData healthData = getGraveHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(1)).hasRecentEmergencyCall();
        verify(helper, times(0)).makeEmergencyCall();
    }

    @Test
    public void handleMessageGoodHealthDataWithoutRecentEmergencyCall() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(false);
        when(helper.makeEmergencyCall()).thenReturn(true);

        HealthData healthData = getGoodHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(0)).hasRecentEmergencyCall();
        verify(helper, times(0)).makeEmergencyCall();
    }

    @Test
    public void handleMessageGoodHealthDataWithRecentEmergencyCall() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(true);
        when(helper.makeEmergencyCall()).thenReturn(true);

        HealthData healthData = getGoodHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(0)).hasRecentEmergencyCall();
        verify(helper, times(0)).makeEmergencyCall();
    }

    @Test
    public void handleMessageThrowTimeExceptionDueToLocation() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(false);
        when(helper.makeEmergencyCall()).thenThrow(new TimeoutException());

        HealthData healthData = getGraveHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(1)).hasRecentEmergencyCall();
        verify(helper, times(1)).makeEmergencyCall();
    }

    @Test
    public void handleMessageThrowInterruptedExceptionDueToLocation() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(false);
        when(helper.makeEmergencyCall()).thenThrow(new InterruptedException());

        HealthData healthData = getGraveHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(1)).hasRecentEmergencyCall();
        verify(helper, times(1)).makeEmergencyCall();
    }

    @Test
    public void handleMessageThrowEmergencyNumberNotFoundExceptionDueToLocation() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(false);
        when(helper.makeEmergencyCall()).thenThrow(new EmergencyNumberNotFoundException());

        HealthData healthData = getGraveHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(1)).saveHealthData(healthData);
        verify(helper, times(1)).hasRecentEmergencyCall();
        verify(helper, times(1)).makeEmergencyCall();
    }

    @Test
    public void handleMessageThrowInvalidHealthDataExceptionDueToLocation() throws Exception{
        when(helper.hasRecentEmergencyCall()).thenReturn(false);
        when(helper.makeEmergencyCall()).thenReturn(true);

        HealthData healthData = getInvalidHealthData();

        Message message = new Message();
        message.what = MessageType.HEALTH_DATA;
        message.obj = healthData;

        assertFalse(healthDataCallback.handleMessage(message));
        verify(helper, times(0)).saveHealthData(healthData);
        verify(helper, times(0)).hasRecentEmergencyCall();
        verify(helper, times(0)).makeEmergencyCall();
    }


}