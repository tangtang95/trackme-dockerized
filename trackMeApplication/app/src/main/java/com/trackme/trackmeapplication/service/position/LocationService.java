package com.trackme.trackmeapplication.service.position;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;

import java.util.Objects;

public class LocationService extends Service {

    private static final int LOCATION_FOREGROUND_ID = 1339;

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    // Define a listener that responds to location updates
    private LocationListener mLocationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationListener = new UserLocationListener(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action == null) {
                return super.onStartCommand(intent, flags, startId);
            }

            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService() {
        Toast.makeText(getApplicationContext(), R.string.foreground_start_toast_message, Toast.LENGTH_SHORT).show();
        setUpGPS();
    }

    private void stopForegroundService() {
        Log.d(getString(R.string.debug_tag), getString(R.string.stop_foreground));
        Toast.makeText(getApplicationContext(), R.string.foreground_stop_toast_message, Toast.LENGTH_SHORT).show();
        stopForeground(true);
        stopSelf();
    }

    /**
     * Set up the GPS service, which will get the location
     */
    private void setUpGPS() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
                startNotification(getString(R.string.location_notification_text));
            }
        }
    }

    private void startNotification(String contentText){
        Intent notificationIntent = new Intent(this, UserHomeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = startNotificationForegroundWithChannel(pendingIntent, contentText);
        }else {
            notification = startNotificationForegroundWithoutChannel(pendingIntent, contentText);
        }
        startForeground(LOCATION_FOREGROUND_ID, notification);
    }

    /**
     * Start the notification foreground w/o a notification channel for devices with API before Oreo
     */
    private Notification startNotificationForegroundWithoutChannel(PendingIntent pendingIntent, String contentText) {
        return buildNotification(new Notification.Builder(this), pendingIntent, contentText);
    }


    /**
     * Start the notification foreground with a notification channel for devices with API Oreo or
     * after
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification startNotificationForegroundWithChannel(PendingIntent pendingIntent, String contentText) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = getString(R.string.location_channel_id);
        CharSequence channelName = getString(R.string.location_channel_name);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);

        return buildNotification(new Notification
                .Builder(this, channelId), pendingIntent, contentText);
    }

    private Notification buildNotification(Notification.Builder builder, PendingIntent pendingIntent, String contentText) {
        return builder.setContentTitle(getText(R.string.notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setContentText(contentText)
                .setTicker(getText(R.string.ticker_text))
                .build();
    }

}
