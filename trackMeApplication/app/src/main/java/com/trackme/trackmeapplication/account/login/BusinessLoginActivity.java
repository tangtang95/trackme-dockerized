package com.trackme.trackmeapplication.account.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.home.businessHome.BusinessHomeActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * BusinessLoginActivity extends the abstract class LoginActivity and it allows to the third party,
 * like third party user and company to login into the application.
 *
 * @author Mattia Tibaldi
 * @see LoginActivity
 **/
public class BusinessLoginActivity extends LoginActivity{

    @BindView(R.id.editTextMail) protected EditText mail;
    @BindView(R.id.password_visibility)
    protected ImageView passwordVisibility;

    /**
     * Method call when this activity is created. It initialize the share data param with the
     * current sharedPreference.
     *
     * @param savedInstanceState last saved instant state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);

        sp = getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME,MODE_PRIVATE);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, BusinessHomeActivity.class);
        Intent finishIntent = new Intent(Constant.FINISH_ACTION);
        sendBroadcast(finishIntent);
        startActivity(intent);
        finish();
    }

    @Override
    public void saveUserSession() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constant.BUSINESS_LOGGED_BOOLEAN_VALUE_KEY,true);
        editor.apply();
    }

    @Override
    public void navigateToBusinessLogin() {
        throw new IllegalStateException();
    }

    @Override
    public void setLoginError() {
        password.setError(getString(R.string.business_login_error));
    }

    @Override
    public void setMailError() {
        mail.setError(getString(R.string.email_is_not_valid));
    }

    /**
     * It handles the third party login button click event and it delegate the validation to the
     * LoginDelegate.
     */
    @OnClick(R.id.businessLoginButton)
    public void onBusinessLoginButtonClick() {
        mDelegate.businessLogin(mail.getText().toString(), password.getText().toString(), this);
    }

}
