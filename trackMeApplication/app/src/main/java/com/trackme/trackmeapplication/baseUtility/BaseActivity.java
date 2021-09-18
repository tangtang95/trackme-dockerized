package com.trackme.trackmeapplication.baseUtility;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base activity class. It is an abstract class that implements the mvp pattern. Provide the bind of
 * the activity with ButterKnife library.
 *
 * @author Mattia Tibaldi
 *
 * @param <P> Base presenter.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView {

    private Unbinder mUnBinder = null;

    private ProgressBar progressBar = null;

    /**
     * Getter method. Return the activity's presenter.
     *
     * @return the current presenter of this view.
     */
    protected @NonNull abstract P getPresenterInstance();

    protected P mPresenter = null;

    /**
     * Attach the activity to its presenter.
     *
     * @param savedInstanceState the last saved instance state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
    }

    /**
     * Set the activity layout and bind the view.
     *
     * @param layoutResID activity layout.
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnBinder = ButterKnife.bind(this);
    }

    /**
     * Unbind the activity.
     */
    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mUnBinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showProgress(){
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(){
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public View getContentView() {
        return getWindow().getDecorView();
    }

    /**
     * Getter method.
     *
     * @return the progress bar. Null if the progress bar is not instantiate in the layout.
     */
    protected ProgressBar getProgressBar(){
        return progressBar;
    }

    /**
     * Getter method.
     *
     * @return the binder for tis activity.
     */
    protected Unbinder getmUnBinder() {
        return mUnBinder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !((Object) this).getClass().getSimpleName().equals(o.getClass().getSimpleName())) return false;
        BaseActivity<?> that = (BaseActivity<?>) o;
        return Objects.equals(mUnBinder, that.mUnBinder) &&
                Objects.equals(progressBar, that.progressBar) &&
                Objects.equals(mPresenter, that.mPresenter);
    }

}
