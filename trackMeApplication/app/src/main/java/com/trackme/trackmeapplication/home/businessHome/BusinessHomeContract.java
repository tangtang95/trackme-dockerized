package com.trackme.trackmeapplication.home.businessHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

/**
 * Class with the two principal interfaces of BusinessHomeActivity (mvp pattern)
 * A contract class defines constants that help applications work with the content URIs,
 * column names, intent actions, and other features of a content provider.
 *
 * @author Mattia Tibaldi
 */
class BusinessHomeContract {

    /**
     * BusinessView contract with the main function that it should implement.
     */
    interface BusinessHomeView extends BaseView {

        /**
         * It starts a new BusinessProfileActivity and it shows it to the user.
         */
        void navigateToBusinessProfile();

        /**
         * It starts a new BusinessSettingsActivity and it shows it to the user.
         */
        void navigateToBusinessSettings();

        /**
         * It starts a new userLoginActivity and it shows it to the user.
         */
        void navigateToUserLogin();

        BusinessHomeActivity getActivity();

        /**
         * Getter method.
         *
         * @return the third party mail used in the login.
         */
        String getToken();

    }

    /**
     * Presenter contract with the main function that it should implement.
     */
    interface BusinessHomePresenter extends BasePresenter<BusinessHomeView> {

        /**
         * Call when a profile button is pressed in the menu
         */
        void onProfileSelected();

        /**
         * Call when a settings button is pressed in the menu
         */
        void onSettingsSelected();

        /**
         * Call when a logout button is pressed in the menu
         */
        void onLogoutSelected();

    }
}
