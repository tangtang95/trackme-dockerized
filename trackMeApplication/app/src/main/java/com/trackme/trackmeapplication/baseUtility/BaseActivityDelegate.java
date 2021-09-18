package com.trackme.trackmeapplication.baseUtility;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base activity delegate. This class implement the delegate pattern, the main view call the delegate
 * for delegating some actions and then the delegate show the modification with mvp pattern.
 *
 * @author Mattia Tibaldi
 *
 * @param <V> Icurrent view interface for the activity.
 * @param <P> Implementation of a presenter (mvp)
 *
 */
public abstract class BaseActivityDelegate<
        V extends BaseView,
        P extends BasePresenterImpl<V>> {

    private Unbinder mUnBinder = null;

    protected P mPresenter;

    /**
     * Set the presenter instance in this class
     *
     * @param presenter the presenter used for showing the modification on the main view.
     */
    public void onCreate(P presenter) {
        mPresenter = presenter;
        mUnBinder = ButterKnife.bind(this, mPresenter.getView().getContentView());
    }

    /**
     * When onDestroy is call for the main activity, the unbind of the view is performed.
     */
    void onDestroy() {
        mUnBinder.unbind();
    }

    /**
     * Getter method
     *
     * @return the binder of the delegate.
     */
    Unbinder getmUnBinder() {
        return mUnBinder;
    }
}
