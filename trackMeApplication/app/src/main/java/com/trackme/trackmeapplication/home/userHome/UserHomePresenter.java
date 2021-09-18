package com.trackme.trackmeapplication.home.userHome;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

/**
 * Presenter class is a middleware class between view and model and call the method of the view
 * when the delegate performs an action. (mvp pattern)
 *
 * @author Mattia Tibaldi
 * @see com.trackme.trackmeapplication.baseUtility.BasePresenterImpl
 * @see UserHomeContract.UserHomePresenter
 */
public class UserHomePresenter extends BasePresenterImpl<
        UserHomeContract.UserHomeView> implements UserHomeContract.UserHomePresenter {

    @Override
    public void onProfileSelected() {
        mView.navigateToUserProfile();
    }

    @Override
    public void onLocationSwitch(boolean status) {
        if (status)
            mView.startLocationService();
        else
            mView.stopLocationService();
    }

    @Override
    public void onHealthSwitch(boolean status) {
        if (status)
            mView.startHealthService();
        else
            mView.stopHealthService();
    }

    @Override
    public void onSettingsSelected() {
        mView.navigateToUserSettings();
    }

    @Override
    public void onLogoutSelected() {
        mView.navigateToUserLogin();
    }

    @Override
    public void onConnectionError() {
        mView.showMessage(mView.getContentView().getResources().getString(R.string.connection_error));
    }
}
