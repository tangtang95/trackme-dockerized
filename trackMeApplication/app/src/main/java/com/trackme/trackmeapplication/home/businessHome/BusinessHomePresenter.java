package com.trackme.trackmeapplication.home.businessHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

/**
 * Presenter class is a middleware class between view and model and call the method of the view
 * when the delegate performs an action. (mvp pattern)
 *
 * @author Mattia Tibaldi
 * @see com.trackme.trackmeapplication.baseUtility.BasePresenterImpl
 * @see BusinessHomeContract.BusinessHomePresenter
 */
public class BusinessHomePresenter extends BasePresenterImpl<BusinessHomeContract.BusinessHomeView>
    implements BusinessHomeContract.BusinessHomePresenter {

    @Override
    public void onProfileSelected() {
        mView.navigateToBusinessProfile();
    }

    @Override
    public void onSettingsSelected() {
        mView.navigateToBusinessSettings();
    }

    @Override
    public void onLogoutSelected() {
        mView.navigateToUserLogin();
    }
}
