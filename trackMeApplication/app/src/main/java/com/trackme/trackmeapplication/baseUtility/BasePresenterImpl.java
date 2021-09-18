package com.trackme.trackmeapplication.baseUtility;

import java.util.Objects;

/**
 * Implementation of the base presenter.
 *
 * @author Mattia Tibaldi
 *
 * @param <V>
 */
public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V>{

    protected V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    /**
     * Getter method.
     *
     * @return the attach view.
     */
    public V getView() {
        return mView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().getSimpleName().equals(o.getClass().getSimpleName())) return false;
        BasePresenterImpl<?> that = (BasePresenterImpl<?>) o;
        return Objects.equals(mView, that.mView);
    }
}
