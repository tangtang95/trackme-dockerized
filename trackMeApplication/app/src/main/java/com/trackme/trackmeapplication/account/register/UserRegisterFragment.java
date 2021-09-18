package com.trackme.trackmeapplication.account.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.account.network.AccountNetworkInterface;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * User register fragment is a fragment shown in the registrationActivity and it provides a form for
 * the user registration on the application.
 *
 * @author Mattia Tibaldi
 */
public class UserRegisterFragment extends BaseFragment {

    /*Bind the object on the layout*/
    @BindView(R.id.register_ssn)
    protected EditText ssn;
    @BindView(R.id.register_username)
    protected EditText username;
    @BindView(R.id.register_password)
    protected EditText password;
    @BindView(R.id.register_first_name)
    protected EditText firstName;
    @BindView(R.id.register_last_name)
    protected EditText lastName;
    @BindView(R.id.register_birth_day)
    protected TextView birthDay;
    @BindView(R.id.register_birth_city)
    protected EditText birthCity;
    @BindView(R.id.register_birth_nation)
    protected EditText birthNation;
    @BindView(R.id.password_visibility)
    protected ImageView passwordVisibility;
    @BindView(R.id.accept_terms)
    protected CheckBox terms;


    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_user_register;
    }

    @Override
    protected void setUpFragment() {
        onDateSetListener = (datePicker, year, month, day) -> {
            month++;
            String date = year + "/" + month + "/" + day;
            birthDay.setText(date);
        };

    }

    /**
     * It handles the register button click event.
     */
    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (checkConstraintOnData()) {
            AccountNetworkInterface network = AccountNetworkImp.getInstance();
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(birthDay.getText().toString());
            } catch (ParseException e) {
                showMessage(getString(R.string.date_format_error));
            }
            try {
                network.userSignUp( new User(
                        ssn.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        convertedDate,
                        birthCity.getText().toString(),
                        birthNation.getText().toString())
                );
                ((Activity)getmContext()).finish();
            } catch (UserAlreadySignUpException e) {
                showMessage(getString(R.string.user_with_this_social_security_number_already_exist));
            } catch (ConnectionException e) {
                showMessage(getString(R.string.connection_error));
            }
        }
    }

    /**
     * It handles the password visibility button click event.
     */
    @OnClick(R.id.password_visibility)
    public void onPasswordVisibilityClick(){
        final int TEXT_PASSWORD = 129;

        if (password.getInputType() == TEXT_PASSWORD) {
            password.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordVisibility.setImageResource(R.drawable.ic_visibility);
        }
        else {
            password.setInputType(TEXT_PASSWORD);
            passwordVisibility.setImageResource(R.drawable.ic_visibility_off);
        }
    }

    /**
     * Handle the term and condition click event.
     */
    @OnClick(R.id.textViewTermAndCondition)
    void onTermsAndConditionClick() {
        TermPopUp.showTermPopUp(getmContext());
    }

    /**
     * It handles the birthDay click event and it shows to the user a calendar for
     * selecting the date.
     */
    @OnClick(R.id.register_birth_day)
    public void onBirthDayClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                    getmContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    onDateSetListener,
                    year, month, day);
            if (dialog.getWindow() != null)
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * Control if all the data insert by the user in the registration form are valid.
     *
     * @return true if the data insert are acceptable, false otherwise.
     */
    private boolean checkConstraintOnData() {
        if (ssn.getText().toString().isEmpty() ||
                username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                firstName.getText().toString().isEmpty() ||
                lastName.getText().toString().isEmpty() ||
                birthDay.getText().toString().isEmpty() ||
                birthCity.getText().toString().isEmpty() ||
                birthNation.getText().toString().isEmpty()) {
            showMessage(getString(R.string.no_field_must_be_empty));
            return false;
        }
        if (!ssn.getText().toString().matches(Constant.SSN_PATTERN)) {
            ssn.setError(getString(R.string.ssn_is_not_valid));
            return false;
        }
        if (username.getText().toString().length() > 30) {
            username.setError(getString(R.string.username_is_too_long));
            return false;
        }
        if (!terms.isChecked()) {
            showMessage(getString(R.string.terms_and_condition_error));
            return false;
        }
        return true;
    }

}
