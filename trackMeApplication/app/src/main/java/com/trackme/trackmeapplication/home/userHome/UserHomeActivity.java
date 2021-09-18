package com.trackme.trackmeapplication.home.userHome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.exception.UserAlreadyLogoutException;
import com.trackme.trackmeapplication.account.login.UserLoginActivity;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.account.network.AccountNetworkInterface;
import com.trackme.trackmeapplication.account.register.UserProfileActivity;
import com.trackme.trackmeapplication.baseUtility.BaseDelegationActivity;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.Settings;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;
import com.trackme.trackmeapplication.localdb.database.DatabaseManager;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.localdb.entity.PositionData;
import com.trackme.trackmeapplication.service.health.HealthService;
import com.trackme.trackmeapplication.service.position.LocationService;
import com.trackme.trackmeapplication.sharedData.ClusterDataWrapper;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkInterface;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;

/**
 * User Home class. The main class for the user with its menu for managing request message from the
 * third party and takes health data.
 *
 * @author Mattia Tibaldi
 * @see BaseDelegationActivity
 */
public class UserHomeActivity extends BaseDelegationActivity<
        UserHomeContract.UserHomeView,
        UserHomePresenter,
        UserHomeDelegate> implements UserHomeContract.UserHomeView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tab_layout)protected TabLayout tabLayout;

    private SharedPreferences sp;
    private String token;
    private Handler handler;
    private Thread pushData;

    @NonNull
    @Override
    protected UserHomePresenter getPresenterInstance() {
        return new UserHomePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_home);
        setSupportActionBar(toolbar);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        sp = getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
        token = sp.getString(Constant.SD_USER_TOKEN_KEY, null);

        super.onCreate(savedInstanceState);

        mDelegate.configureToolbar();

        handler = new Handler();
        pushData = new Thread(){
            @Override
            public void run() {
                super.run();
                new PushDataAsyncTask(getActivity()).execute();
                handler.postDelayed(this, Settings.getPushDataTime());
            }
        };
        handler.post(pushData);
    }

    @Override
    protected UserHomeDelegate instantiateDelegateInstance() {
        return new UserHomeDelegate();
    }

    @Override
    public void onBackPressed() {
        if (!mDelegate.closeDrawer()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        token = sp.getString(Constant.SD_USER_TOKEN_KEY, null);
        super.onResume();
    }

    @Override
    public void navigateToUserProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToUserSettings() {
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToUserLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        AccountNetworkInterface accountNetwork = AccountNetworkImp.getInstance();
        try {
            accountNetwork.userLogout(token);
            sp.edit().putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, false).apply();
            startActivity(intent);
            finish();
        } catch (UserAlreadyLogoutException e) {
            sp.edit().putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, false).apply();
            startActivity(intent);
            finish();
        } catch (ConnectionException e) {
            showMessage(getString(R.string.connection_error));
        }
    }

    @Override
    public void startLocationService() {
        // TODO synchronize slider with service
        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.setAction(LocationService.ACTION_START_FOREGROUND_SERVICE);
        startService(serviceIntent);
    }

    @Override
    public void stopLocationService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
        startService(serviceIntent);
    }

    @Override
    public void startHealthService() {
        // TODO synchronize slider with service
        Intent serviceIntent = new Intent(this, HealthService.class);
        serviceIntent.setAction(HealthService.ACTION_START_FOREGROUND_SERVICE);
        serviceIntent.putExtra(getString(R.string.birth_year_key), sp.getString(Constant.BIRTH_DATE_KEY, null));
        startService(serviceIntent);
    }

    @Override
    public void stopHealthService() {
        Intent serviceIntent = new Intent(this, HealthService.class);
        serviceIntent.setAction(HealthService.ACTION_STOP_FOREGROUND_SERVICE);
        startService(serviceIntent);
    }

    @Override
    public UserHomeActivity getActivity() {
        return this;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(pushData);
        super.onDestroy();
    }


    /**
     * asyncTask for sending data to the server.
     *
     * @author Mattia Tibaldi
     */
    private static class PushDataAsyncTask extends AsyncTask<String, Void, String> {

        private WeakReference<UserHomeActivity> weakReference;
        private String token;

        PushDataAsyncTask(UserHomeActivity context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            UserHomeActivity userHomeActivity = weakReference.get();
            SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();
            String message = null;

            if (userHomeActivity == null)
                return "Error in load database";

            token = userHomeActivity.getToken();

            AppDatabase appDatabase = DatabaseManager.getInstance(userHomeActivity.getApplicationContext());
            appDatabase.beginTransaction();
            List<HealthData> healthDataList = appDatabase.getHealthDataDao().getAll();
            List<PositionData> positionDataList = appDatabase.getPositionDataDao().getAll();

            ClusterDataWrapper clusterData = new ClusterDataWrapper();
            for (HealthData hd: healthDataList) {
                clusterData.addHealthData(hd);
            }

            for (PositionData pd: positionDataList){
                clusterData.addPositionData(pd);
            }

            try {
                sharedDataNetwork.sendClusterData(token, clusterData);
                appDatabase.getHealthDataDao().deleteAll();
                appDatabase.getPositionDataDao().deleteAll();
            } catch (ConnectionException e) {
                message = userHomeActivity.getString(R.string.connection_error);
            }
            appDatabase.endTransaction();

            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            UserHomeActivity userHomeActivity = weakReference.get();
            if (message != null && userHomeActivity != null)
                Toast.makeText(userHomeActivity, message, Toast.LENGTH_SHORT).show();
        }
    }
}
