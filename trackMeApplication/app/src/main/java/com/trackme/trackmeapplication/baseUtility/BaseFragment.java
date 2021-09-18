package com.trackme.trackmeapplication.baseUtility;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base fragment is an abstract class that implement some useful method for the fragment and provide
 * the bind with ButterKnife.
 *
 * @author Mattia Tibaldi
 */
public abstract class BaseFragment extends Fragment {

    private Context mContext = getContext();

    private Unbinder mUnBinder = null;

    /**
     * Getter method.
     *
     * @return the id of the layout of the fragment.
     */
    protected abstract int getLayoutResID();

    /**
     * In this method the programmer can put some function for setting up of the fragment.
     */
    protected abstract void setUpFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResID(), container, false);
        mUnBinder = ButterKnife.bind(this, v);
        setUpFragment();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mContext = null;
        mUnBinder.unbind();
        super.onDetach();
    }

    /**
     * Show a message on screen.
     *
     * @param message to show.
     */
    public void showMessage(String message) {
        Toast.makeText(getmContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter method
     *
     * @return the current context. The context is set when the fragment is attached to its activity.
     */
    public Context getmContext(){
        return mContext;
    }

    /**
     * Create a file in download folder and it writes into some data.
     *
     * @param sFileName name of file
     * @param sBody content of file
     */
    public void generateNoteOnSD(String sFolderName, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),sFolderName);
            if (!root.exists()) {
                final boolean mkdirs = root.mkdirs();
            }
            File file = new File(root, sFileName);
            FileWriter writer = new FileWriter(file);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(mContext, getString(R.string.download_file), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext, getString(R.string.download_file_error), Toast.LENGTH_SHORT).show();
        }
    }

}
