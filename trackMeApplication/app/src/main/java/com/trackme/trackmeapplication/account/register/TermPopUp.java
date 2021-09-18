package com.trackme.trackmeapplication.account.register;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Term and condition view.
 *
 * @author Mattia Tibaldi
 */
public class TermPopUp {

    /**
     * Constructor.
     */
    public TermPopUp(){}

    /**
     * Show the legal note on screen.
     *
     * @param context current context
     */
    @SuppressLint("ClickableViewAccessibility")
    static void showTermPopUp(@NonNull Context context) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.term_popup, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(new LinearLayout(context), Gravity.CENTER, 0, 0);
            popupWindow.setElevation(20);

            // dismiss the popup window when touched
            popupView.setOnTouchListener((v, event) -> {
                popupWindow.dismiss();
                return true;
            });
        } else
            Toast.makeText(context, context.getString(R.string.legal_note_show_error), Toast.LENGTH_SHORT).show();
    }
}
