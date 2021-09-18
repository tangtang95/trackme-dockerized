package com.trackme.trackmeapplication.service.health;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.login.BusinessLoginActivity;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.Matchers.not;

@RunWith(Enclosed.class)
public class HealthServiceHelperImplTest {

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
        public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        @Rule
        public ServiceTestRule mServiceTestRule = new ServiceTestRule();

        @Rule
        public IntentsTestRule<BusinessLoginActivity> mActivity = new IntentsTestRule<>(BusinessLoginActivity.class);

        @Spy
        private HealthService androidService;

        private HealthServiceHelperImpl helper;

        @Before
        public void setUp() throws Exception {
            intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

            Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), HealthService.class);
            IBinder binder = mServiceTestRule.bindService(serviceIntent);
            androidService = ((HealthService.LocalBinder) binder).getService();
            mServiceTestRule.startService(serviceIntent);

            AppDatabase appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase.class).build();

            helper = new HealthServiceHelperImpl(androidService, appDatabase);
        }

        @After
        public void tearDown() throws Exception {
            androidService = null;
            helper = null;
        }

        @Test
        public void makeEmergencyCall() throws Exception {
            mockUpGpsLocation(47D, 12D, 1);
            helper.makeEmergencyCall();
            intended(hasAction(Intent.ACTION_CALL), times(1));
        }

    }
}