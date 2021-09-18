package com.trackme.trackmeapplication.account.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.httpConnection.Settings;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Address server popUp is a popUp that allows user to insert address ip of server
 *
 * @author Mattia Tibaldi
 */
public class AddressServerPopUp {


    /**
     * Constructor.
     */
    public AddressServerPopUp(){}

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
            @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.server_address_popup, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            EditText editText = popupView.findViewById(R.id.editTextAddress);
            Button button = popupView.findViewById(R.id.button_set_address);

            popupWindow.showAtLocation(new LinearLayout(context), Gravity.TOP, 0, 0);
            popupWindow.setElevation(20);
            editText.setHint(Settings.getServerAddress());

            button.setOnClickListener(view -> {
                Settings.setServerAddress(editText.getText().toString());
                popupWindow.dismiss();
            });

            // dismiss the popup window when touched
            popupView.setOnTouchListener((v, event) -> {
                popupWindow.dismiss();
                return true;
            });
        } else
            Toast.makeText(context, context.getString(R.string.legal_note_show_error), Toast.LENGTH_SHORT).show();
    }



}
