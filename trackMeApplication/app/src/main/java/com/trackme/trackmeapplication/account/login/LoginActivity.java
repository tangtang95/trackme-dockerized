package com.trackme.trackmeapplication.account.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.register.RegisterActivity;
import com.trackme.trackmeapplication.baseUtility.BaseDelegationActivity;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.Settings;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * LoginActivity class. This abstract class implement some useful method for the user
 * login. It extends BaseDelegationActivity.
 *
 * @author Mattia Tibaldi
 * @see BaseDelegationActivity
 * @see LoginContract
 */
public abstract class LoginActivity extends BaseDelegationActivity<
        LoginContract.LoginView,
        LoginPresenter,
        LoginDelegate>
        implements LoginContract.LoginView {

    @BindView(R.id.editTextPass) protected EditText password;
    @BindView(R.id.password_visibility)
    protected ImageView passwordVisibility;

    /**
     * This app uses a sharePreference for storing some value in other to share data with
     * other activity. The value stored are username, email and boolean values that indicate if a
     * user is logged.
     */
    protected SharedPreferences sp;

    /**
     * Getter method.
     *
     * @return a new presenter for this activity;
     */
    @NonNull
    @Override
    protected LoginPresenter getPresenterInstance() {
        return new LoginPresenter();
    }

    /**
     * Getter method.
     *
     * @return a new delegate for this activity.
     */
    @Override
    protected LoginDelegate instantiateDelegateInstance() {
        return new LoginDelegate();
    }

    /**
     * It starts, without finish the first, a new RegisterActivity and it shows it to the user.
     */
    @Override
    public void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * It handles the password visibility button click event.
     */
    @OnClick(R.id.password_visibility)
    public void onPasswordVisibilityClick(){
        final int TEXT_PASSWORD = 129;

        if (password.getInputType() == TEXT_PASSWORD) {
            password.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordVisibility.setImageResource(R.drawable.ic_visibility_white);
        }
        else {
            password.setInputType(TEXT_PASSWORD);
            passwordVisibility.setImageResource(R.drawable.ic_visibility_off_white);
        }
    }

    /**
     * It handles the event click on the textView "Register" by using the mvp pattern.
     */
    @OnClick(R.id.textViewRegister)
    public void onTextViewRegisterClick() {
        sp.edit().putString(Constant.SD_SERVER_ADDRESS_KEY, Settings.getServerAddress()).apply();
        mPresenter.register();
    }

}

