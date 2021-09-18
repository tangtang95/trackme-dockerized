package com.trackme.trackmeapplication.home.userHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

/**
 * Class with the two principal interfaces of UserHomeActivity (mvp pattern)
 * A contract class defines constants that help applications work with the content URIs,
 * column names, intent actions, and other features of a content provider.
 *
 * @author Mattia Tibaldi
 */
class UserHomeContract {

    /**
     * User home expose the main function of the user Home
     */
    interface UserHomeView extends BaseView {

        /**
         * It starts a new UserProfileActivity and it shows it to the user.
         */
        void navigateToUserProfile();

        /**
         * It starts a new UserSettingsActivity and it shows it to the user.
         */
        void navigateToUserSettings();

        /**
         * It starts a new UserLoginActivity and it shows it to the user.
         */
        void navigateToUserLogin();

        /**
         * It starts the location service in background.
         */
        void startLocationService();

        /**
         * It stops the location service in background.
         */
        void stopLocationService();

        /**
         * It starts the bluetooth service in background.
         */
        void startHealthService();

        /**
         * It stops the bluetooth service in background.
         */
        void stopHealthService();

        /**
         * Getter method.
         *
         * @return the current activity.
         */
        UserHomeActivity getActivity();

        /**
         * Getter method.
         *
         * @return the username used in the login form.
         */
        String getToken();

    }

    /**
     * Presenter that expose the main function of the user home presenter.
     */
    interface UserHomePresenter extends BasePresenter<UserHomeView> {

        /**
         * Call when the user click on the profile in the menu
         */
        void onProfileSelected();

        /**
         * Call when the user click on the location switch
         */
        void onLocationSwitch(boolean status);

        /**
         * Call when the user click on the bluetooth switch
         */
        void onHealthSwitch(boolean status);

        /**
         * Call when the user click on settings in the menu
         */
        void onSettingsSelected();

        /**
         * Call when the user click on logout in the menu
         */
        void onLogoutSelected();

        /**
         * Call when a connection error event is launched.
         */
        void onConnectionError();

    }
}
