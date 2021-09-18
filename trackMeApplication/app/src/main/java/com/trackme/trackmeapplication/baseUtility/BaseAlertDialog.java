package com.trackme.trackmeapplication.baseUtility;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Base alert dialog is a class that create and show an alert message on the mobile screen. When
 * this class is instantiate maybe the application is in fatal error state.
 *
 * @author Mattia Tibaldi
 */
public class BaseAlertDialog {

    private AlertDialog.Builder builder;

    /**
     * Constructor.
     *
     * @param context current context.
     * @param message error message showed in the alert.
     * @param title title of the alert dialog.
     */
    public BaseAlertDialog(Context context, String message, String title) {

        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> System.exit(0))
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    /**
     * Show the alert dialog on the screen.
     */
    public void show() {
        builder.show();
    }

}
