package com.trackme.trackmeapplication.baseUtility;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Base delegation activity implement the delegate pattern.
 *
 * @author Mattia Tibaldi
 *
 * @param <V> current view interface for the activity.
 * @param <P> Presenter implementation.
 * @param <D> Delegate implementation.
 */
public abstract class BaseDelegationActivity <
        V extends BaseView,
        P extends BasePresenterImpl<V>,
        D extends BaseActivityDelegate<V, P>>
        extends BaseActivity<P> {

    protected D mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate = instantiateDelegateInstance();
        mDelegate.onCreate(mPresenter);
    }

    /**
     * Getter method.
     *
     * @return the delegate class attach to the activity
     */
    protected abstract D instantiateDelegateInstance();

    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

}
