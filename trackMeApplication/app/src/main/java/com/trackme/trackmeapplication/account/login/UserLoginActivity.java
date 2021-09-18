package com.trackme.trackmeapplication.account.login;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.exception.UserAlreadyLogoutException;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.baseUtility.BaseAlertDialog;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.home.businessHome.BusinessHomeActivity;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;
import com.trackme.trackmeapplication.httpConnection.SSL;
import com.trackme.trackmeapplication.httpConnection.Settings;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * UserLoginActivity extends the abstract class LoginActivity and it allows to the user to
 * login into the application. It is the first Activity that starts when the user install
 * the apk on his mobile device.
 *
 * @author Mattia Tibaldi
 * @see LoginActivity
 **/
public class UserLoginActivity extends LoginActivity {

    @BindView(R.id.editTextUser)
    protected EditText username;

    public static final int INITIAL_REQUEST = 1000;

    /**
     * True if the broadcast receiver is registered, false otherwise.
     */
    private boolean isRegister;

    /**
     * it manages the interaction with other activity like businessLoginActivity. it is used mainly
     * for finishing an activity from an other.
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        /**
         * It stops the activity when the action sent is "finish_activity"
         *
         * @param arg0 the sender intent.
         * @param intent action to perform.
         */
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Constant.FINISH_ACTION)) {
                unregisterReceiver(this);
                isRegister = false;
                finish();
            } else {
                BaseAlertDialog alertDialog = new BaseAlertDialog(
                        arg0,
                        getString(R.string.broadcast_receiver_error),
                        getString(R.string.fatal_error_title));
                alertDialog.show();
            }
        }
    };

    /**
     * Method call when this activity is created. It checks the past user session for verifying
     * if the user has already logged and it chooses the best activity to start.
     *
     * @param savedInstanceState last saved instant state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        isRegister = false;
        sp = getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
        Settings.setServerAddress(sp.getString(Constant.SD_SERVER_ADDRESS_KEY, "127.0.0.1"));

        //Fake status for debug
        //sp.edit().putBoolean(Constant.BUSINESS_LOGGED_BOOLEAN_VALUE_KEY, true).apply();
        //sp.edit().putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, true).apply();

        //get app permission
        getPermission();

        //load the keystore
        SSL ssl = SSL.getInstance();
        ssl.setUpSSLConnection(getResources().openRawResource(
                getResources().getIdentifier("keystore",
                        "raw", getPackageName())));

        if (sp.getBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, false)) {
            AccountNetworkImp accountNetworkImp = AccountNetworkImp.getInstance();
            String token = sp.getString(Constant.SD_USER_TOKEN_KEY, null );
            try {
                accountNetworkImp.userLogout(token);
                sp.edit().putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, false).apply();
            } catch (UserAlreadyLogoutException e) {
                sp.edit().putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, false).apply();
            } catch (ConnectionException e) {
                showMessage(getString(R.string.connection_error));
            }
        } else if (sp.getBoolean(Constant.BUSINESS_LOGGED_BOOLEAN_VALUE_KEY, false)){
            AccountNetworkImp accountNetworkImp = AccountNetworkImp.getInstance();
            String token = sp.getString(Constant.SD_BUSINESS_TOKEN_KEY, null );
            try {
                accountNetworkImp.thirdPartyLogout(token);
                sp.edit().putBoolean(Constant.BUSINESS_LOGGED_BOOLEAN_VALUE_KEY, false).apply();
            } catch (UserAlreadyLogoutException e) {
                sp.edit().putBoolean(Constant.BUSINESS_LOGGED_BOOLEAN_VALUE_KEY, false).apply();
            } catch (ConnectionException e) {
                showMessage(getString(R.string.connection_error));
            }
        }

    }



    /**
     * Request all the permission for the correct behavior of the application
     */
    private void getPermission() {
        List<String> stockList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.BLUETOOTH);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.CALL_PHONE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.INTERNET);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            stockList.add(Manifest.permission.ACCESS_NETWORK_STATE);

        String[] permission = stockList.toArray(new String[0]);
        if (permission.length != 0)
            ActivityCompat.requestPermissions(this,
                    permission,
                    INITIAL_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case INITIAL_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    finish();
                break;
        }
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
        if (isRegister)
            unregisterReceiver(broadcastReceiver);
        finish();
    }


    /**
     * It starts a new BusinessHomeActivity and it shows it to the user. At the end it stops this
     * activity.
     */
    private void navigateToBusinessHome() {
        Intent intent = new Intent(this, BusinessHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.imageView)
    public void onLogoCLick(){
        AddressServerPopUp.showTermPopUp(this);
    }

    @Override
    public void saveUserSession() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, true);
        editor.apply();
    }

    @Override
    public void navigateToBusinessLogin() {
        Intent intent = new Intent(this, BusinessLoginActivity.class);
        if (!isRegister) {
            registerReceiver(broadcastReceiver, new IntentFilter(Constant.FINISH_ACTION));
            isRegister = true;
        }
        startActivity(intent);
    }


    @Override
    public void setLoginError() {
        password.setError(getString(R.string.user_login_error));
    }

    @Override
    public void setMailError() {
        throw new IllegalStateException();
    }

    /**
     * It handles the user login button click event and it delegate the validation to the
     * LoginDelegate.
     */
    @OnClick(R.id.userLoginButton)
    public void onUserLoginButtonClick() {
        sp.edit().putString(Constant.SD_SERVER_ADDRESS_KEY, Settings.getServerAddress()).apply();
        mDelegate.userLogin(username.getText().toString(), password.getText().toString(), this);
    }

    /**
     * It handles the third party login click event.
     */
    @OnClick(R.id.textViewThirdPartyLogin)
    public void onTextViewThirdPartyLoginClick() {
        sp.edit().putString(Constant.SD_SERVER_ADDRESS_KEY, Settings.getServerAddress()).apply();
        mPresenter.businessLogin();
    }

    @Override
    protected void onResume() {
        if (isRegister)
            unregisterReceiver(broadcastReceiver);
        isRegister = false;
        super.onResume();
    }
}
