package com.trackme.trackmeapplication.baseUtility;

/**
 * Base presenter interface exposes the two main function for implementing the mvp pattern.
 *
 * @author Mattia Tibaldi
 *
 * @param <V> current view interface for the activity.
 */
public interface BasePresenter<V extends BaseView> {

    /**
     * Attach the view to the presenter.
     *
     * @param view current view to attach.
     */
    void attachView(V view);

    /**
     * Detach the view from the presenter.
     */
    void detachView();

}
