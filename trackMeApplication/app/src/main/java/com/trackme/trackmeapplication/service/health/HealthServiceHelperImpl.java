package com.trackme.trackmeapplication.service.health;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;
import com.trackme.trackmeapplication.localdb.entity.EmergencyCall;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.exception.NoPermissionException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HealthServiceHelperImpl implements HealthServiceHelper {

    private HealthService service;
    private AppDatabase appDatabase;

    public HealthServiceHelperImpl(HealthService service, AppDatabase appDatabase) {
        this.service = service;
        this.appDatabase = appDatabase;
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public Date getUserBirthDate() {
        return service.getUserBirthDate();
    }

    @Override
    public void setUserBirthDate(Date birthDate) {
        service.setUserBirthDate(birthDate);
    }

    @Override
    public void saveHealthData(HealthData healthData) {
        Log.d(service.getString(R.string.debug_tag), healthData.toString());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> appDatabase.getHealthDataDao().insert(healthData));
    }

    @Override
    public boolean hasRecentEmergencyCall() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> numberRecentCallFuture = executor.submit(()-> appDatabase.getEmergencyCallDao().getNumberOfRecentCalls());
        return numberRecentCallFuture.get(1, TimeUnit.SECONDS) > 0;
    }

    @Override
    public boolean makeEmergencyCall() throws InterruptedException, TimeoutException, NoPermissionException, EmergencyNumberNotFoundException {
        if (ActivityCompat.checkSelfPermission(service.getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionException();
        }

        // Make call
        String phoneNumber = service.getEmergencyRoomNumber(getCurrentCountryCode());
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: FAKE " + phoneNumber));
        List<ResolveInfo> activityList = service.getPackageManager().queryIntentActivities(callIntent, 0);
        ResolveInfo app = activityList.get(0);
        callIntent.setClassName(app.activityInfo.packageName, app.activityInfo.name);
        service.startActivity(callIntent);

        // Save Emergency call in the DB
        EmergencyCall emergencyCall = new EmergencyCall();
        emergencyCall.setPhoneNumber(phoneNumber);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

        // Roll back by one hour because of SQLite now function
        calendar.add(Calendar.HOUR, -1);
        emergencyCall.setTimestamp(new Timestamp(calendar.getTime().getTime()));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> appDatabase.getEmergencyCallDao().insert(emergencyCall));
        executor.awaitTermination(1, TimeUnit.SECONDS);
        return true;
    }

    private String getCurrentCountryCode() throws TimeoutException, NoPermissionException {
        return service.getCountryCode(service.getUserLocation());
    }
}
