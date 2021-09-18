package com.trackme.trackmeapplication.account.login;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

/**
 * Class with the two principal interfaces of loginActivity (mvp pattern)
 * A contract class defines constants that help applications work with the content URIs,
 * column names, intent actions, and other features of a content provider.
 *
 * @author Mattia Tibaldi
 */
class LoginContract {

    /**
     * View contract with the main function that it should implement.
     */
    interface LoginView extends BaseView {

        /**
         * It starts a new UserHomeActivity and it shows it to the user. At the and it finishes this
         * activity.
         */
        void navigateToHome();

        /**
         * Set the boolean value of the "user/business_logged" to true if the user is logged and save
         * the username/mail in shareData.
         */
        void saveUserSession();

        /**
         * It starts a new RegisterActivity and it shows it to the user.
         */
        void navigateToRegister();

        /**
         * It starts a new BusinessLoginActivity and it shows it to the user.
         */
        void navigateToBusinessLogin();

        /**
         * It shows an error to the user if username and password do not match.
         */
        void setLoginError();

        /**
         * It shows an error to the user if mail and password do not match.
         */
        void setMailError();
    }

    /**
     * Presenter contract with the main function that it should implement.
     */
    interface LoginPresenter extends BasePresenter<LoginView> {

        /**
         * Call when the user wants to register to the application
         */
        void register();

        /**
         * Call when a user want to login as business user.
         */
        void businessLogin();

        /**
         * Call when a login error event is launched.
         */
        void onLoginError();

        /**
         * Call when a connection error event is launched.
         */
        void onConnectionError();

        /**
         * Call when a login is correctly performed.
         */
        void onLoginSuccess();

        /**
         * Call when a mail login error event is launched.
         */
        void onMailError();

    }
}
