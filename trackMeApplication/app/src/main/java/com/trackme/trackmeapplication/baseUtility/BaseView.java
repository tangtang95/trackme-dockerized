package com.trackme.trackmeapplication.baseUtility;

import android.view.View;

/**
 * Base view interface exposes the main function of the base activity.
 */
public interface BaseView {

    /**
     * Show a progress bar on the screen.
     */
    void showProgress();

    /**
     * Hide the progress bar from the screen
     */
    void hideProgress();

    /**
     * Show a message on the screen.
     *
     * @param message to show
     */
    void showMessage(String message);

    /**
     * Getter method.
     *
     * @return the current view.
     */
    View getContentView();
}
