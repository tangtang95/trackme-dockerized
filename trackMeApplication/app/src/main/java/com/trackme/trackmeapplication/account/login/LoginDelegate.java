package com.trackme.trackmeapplication.account.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.account.network.AccountNetworkInterface;
import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;

/**
 *  Delegate class that performs the validation of the data insert by the user in the login forms.
 *  (delegate pattern)
 *
 * @author Mattia Tibaldi
 * @see BaseActivityDelegate
 */
public class LoginDelegate extends BaseActivityDelegate<LoginContract.LoginView,LoginPresenter> {

    private AccountNetworkInterface network = AccountNetworkImp.getInstance();

    /**
     * Check if the user params are correct and call the method for performing the user login on server.
     *
     * @param username the username insert by user.
     * @param password the password insert by user.
     */
    public void userLogin(final String username, final String password, Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, Context.MODE_PRIVATE);

        if (username.isEmpty() || password.isEmpty())
            mPresenter.onLoginError();
        else {
            try {
                String token = network.userLogin(username, password);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constant.SD_USER_TOKEN_KEY, token).apply();
                mPresenter.onLoginSuccess();
            } catch (InvalidDataLoginException e) {
                mPresenter.onLoginError();
            } catch (ConnectionException e) {
                mPresenter.onConnectionError();
            }
        }
    }

    /**
     * Check if the user params are correct and call the method for performing the third party
     * login on server.
     *
     * @param mail the mail insert by the user.
     * @param password the password insert by the user.
     */
    public void businessLogin(final String mail, final String password, Context context) {

        SharedPreferences sp = context.getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, Context.MODE_PRIVATE);

        if (mail.isEmpty() || password.isEmpty())
            mPresenter.onLoginError();
        else
            if (!isValidEmail(mail))
                mPresenter.onMailError();
            else {
                try {
                    String token = network.thirdPartyLogin(mail, password);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constant.SD_BUSINESS_TOKEN_KEY, token).apply();
                    mPresenter.onLoginSuccess();
                } catch (InvalidDataLoginException e) {
                    mPresenter.onLoginError();
                } catch (ConnectionException e) {
                    mPresenter.onConnectionError();
                }
            }
    }

    /**
     * Check if the mail is a possible mail. Return true if the string pass is a mail.
     *
     * @param target mail to test
     * @return true if the mail is a possible mail or false otherwise.
     */
    private boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && target.matches(Constant.E_MAIL_PATTERN));
    }
}
