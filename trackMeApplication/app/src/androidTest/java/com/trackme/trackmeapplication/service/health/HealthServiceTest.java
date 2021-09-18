package com.trackme.trackmeapplication.service.health;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.internal.deps.guava.base.Stopwatch;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.login.BusinessLoginActivity;
import com.trackme.trackmeapplication.localdb.dao.EmergencyCallDao;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.util.MessageType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class HealthServiceTest {

    private static void mockUpGpsLocation(double latitude, double longitude, float accuracy) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(System.currentTimeMillis());
        location.setAccuracy(accuracy);
        location.setVerticalAccuracyMeters(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(InstrumentationRegistry.getTargetContext());
        fusedLocationClient.setMockMode(true);
        fusedLocationClient.setMockLocation(location);
        fusedLocationClient.flushLocations();
    }

    @RunWith(AndroidJUnit4.class)
    public static class TestWithPermissionGranted {

        @Rule
        public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);

        @Rule
        public ServiceTestRule mServiceTestRule = new ServiceTestRule();

        @Rule
        public IntentsTestRule<BusinessLoginActivity> mActivity = new IntentsTestRule<>(BusinessLoginActivity.class);

        private HealthService healthService;

        @Before
        public void setUp() throws Exception {
            Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), HealthService.class);
            IBinder binder = mServiceTestRule.bindService(serviceIntent);
            healthService = ((HealthService.LocalBinder) binder).getService();
        }

        @After
        public void tearDown() {
            healthService = null;
        }

        @Test
        public void getEmergencyRoomNumberForItaly() throws Exception {
            String emergencyRoomNumber = healthService.getEmergencyRoomNumber("IT");
            assertEquals("118", emergencyRoomNumber);
        }

        @Test(expected = EmergencyNumberNotFoundException.class)
        public void getEmergencyRoomNumberForAfghanistan() throws Exception {
            String emergencyRoomNumber = healthService.getEmergencyRoomNumber("AF");
        }

        @Test(expected = EmergencyNumberNotFoundException.class)
        public void getEmergencyRoomNumberWrongCountryCode() throws Exception {
            String emergencyRoomNumber = healthService.getEmergencyRoomNumber("NO CODE");
        }

        @Test
        public void getUserLocationHasLastLocation() throws Exception {
            mockUpGpsLocation(47D, 10D, 1);
            Location location = healthService.getUserLocation();
            assertEquals(new Double(47), new Double(location.getLatitude()));
            assertEquals(new Double(10), new Double(location.getLongitude()));
            assertEquals(new Float(1), new Float(location.getAccuracy()));
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class IntegrationTesting {

        @Rule
        public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        @Rule
        public ServiceTestRule mServiceTestRule = new ServiceTestRule();

        @Rule
        public IntentsTestRule<BusinessLoginActivity> mActivity = new IntentsTestRule<>(BusinessLoginActivity.class);

        private HealthService healthService;

        private HealthDataCallback healthDataCallback;

        private HealthServiceHelperImpl healthServiceHelper;

        private AppDatabase mockDatabase;

        @Before
        public void setUp() throws Exception {
            LocationManager lm = (LocationManager) mActivity.getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);
            if (lm != null) {
                lm.clearTestProviderEnabled(LocationManager.GPS_PROVIDER);
            }

            intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

            Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), HealthService.class);
            IBinder binder = mServiceTestRule.bindService(serviceIntent);
            healthService = ((HealthService.LocalBinder) binder).getService();
            mServiceTestRule.startService(serviceIntent);

            AppDatabase appDatabase = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase.class,
                    InstrumentationRegistry.getTargetContext().getString(R.string.persistent_database_name)).build();
            appDatabase.clearAllTables();

            mockDatabase = Mockito.mock(AppDatabase.class);
            when(mockDatabase.getPositionDataDao()).thenReturn(appDatabase.getPositionDataDao());
            when(mockDatabase.getHealthDataDao()).thenReturn(appDatabase.getHealthDataDao());
            when(mockDatabase.getEmergencyCallDao()).thenReturn(appDatabase.getEmergencyCallDao());

            healthServiceHelper = new HealthServiceHelperImpl(healthService, mockDatabase);
            healthDataCallback = new HealthDataCallback(healthServiceHelper);
            healthServiceHelper.setUserBirthDate(new Date(0));
        }

        @After
        public void tearDown() {
            healthService = null;
            healthServiceHelper = null;
        }

        /**
         * Test performance of make call
         */
        @Test
        public void testMakeCallPerformance(){
            //To avoid block calls due to recent calls
            when(mockDatabase.getEmergencyCallDao()).thenReturn(mock(EmergencyCallDao.class));

            mockUpGpsLocation(47D, 10D, 1);
            HealthData healthData = new HealthData();
            healthData.setTimestamp(Timestamp.valueOf("2018-10-10 13:00:00"));
            healthData.setHeartbeat(20);
            healthData.setPressureMin(20);
            healthData.setPressureMax(100);
            healthData.setBloodOxygenLevel(90);

            Message message = new Message();
            message.what = MessageType.HEALTH_DATA;
            message.obj = healthData;
            Stopwatch stopwatch = Stopwatch.createUnstarted();
            stopwatch.start();
            int numberOfTests = 10;
            for (int i = 0; i < numberOfTests; i++) {
                healthDataCallback.handleMessage(message);
                intended(hasAction(Intent.ACTION_CALL), times(i+1));
            }

            double averageTime = stopwatch.elapsed(TimeUnit.SECONDS) / ((double) numberOfTests);
            Log.d("DEBUG_TAG", "The average time for executing calls is: " + averageTime);
            assertTrue(averageTime < 5);
        }

        /**
         * Test if a double call consecutively results in only one intent action due to recent calls
         */
        @Test
        public void testDoubleConsecutiveCall(){
            mockUpGpsLocation(47D, 10D, 1);
            HealthData healthData = new HealthData();
            healthData.setTimestamp(Timestamp.valueOf("2018-10-10 13:00:00"));
            healthData.setHeartbeat(20);
            healthData.setPressureMin(20);
            healthData.setPressureMax(100);
            healthData.setBloodOxygenLevel(90);

            Message message = new Message();
            message.what = MessageType.HEALTH_DATA;
            message.obj = healthData;
            healthDataCallback.handleMessage(message);
            healthDataCallback.handleMessage(message);
            intended(hasAction(Intent.ACTION_CALL), times(1));
        }
    }
}