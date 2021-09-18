package com.trackme.trackmeapplication.account.login;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

/**
 * Presenter class is a middleware class between view and model and call the method of the view
 * when the delegate performs an action. (mvp pattern)
 *
 * @author Mattia Tibaldi
 * @see com.trackme.trackmeapplication.baseUtility.BasePresenterImpl
 * @see LoginContract.LoginPresenter
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.LoginView> implements
        LoginContract.LoginPresenter {

    @Override
    public void register() {
        mView.navigateToRegister();
    }

    @Override
    public void businessLogin() {
        mView.navigateToBusinessLogin();
    }

    @Override
    public void onLoginError() {
        mView.setLoginError();
    }

    @Override
    public void onConnectionError() {
        mView.showMessage(mView.getContentView().getResources().getString(R.string.connection_error));
    }

    @Override
    public void onLoginSuccess() {
        mView.saveUserSession();
        mView.navigateToHome();
    }

    @Override
    public void onMailError() {
        mView.setMailError();
    }

}
