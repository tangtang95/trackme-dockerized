package com.trackme.trackmeapplication.account.register;

import android.app.Activity;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.account.network.AccountNetworkInterface;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyCustomer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CompanyDetail register fragment is a fragment shown in the registrationActivity and it provides a form for
 * the company registration on the application.
 *
 * @author Mattia Tibaldi
 */
public class CompanyRegisterFragment extends BaseFragment {

    /*Bind the object on the layout*/
    @BindView(R.id.register_company_name)
    protected EditText companyName;
    @BindView(R.id.register_mail)
    protected EditText mail;
    @BindView(R.id.register_password)
    protected EditText password;
    @BindView(R.id.register_address)
    protected EditText address;
    @BindView(R.id.register_duns_number)
    protected EditText dunsNumber;
    @BindView(R.id.password_visibility)
    protected ImageView passwordVisibility;
    @BindView(R.id.accept_terms)
    protected CheckBox terms;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_company_register;
    }

    @Override
    protected void setUpFragment() {
        /*Not important for this fragment*/
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
     * It handles the register button click event.
     */
    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (checkConstraintOnData()) {
            AccountNetworkInterface network = AccountNetworkImp.getInstance();
            try {
                network.companySignUp( new CompanyDetail(
                        companyName.getText().toString(),
                        new ThirdPartyCustomer(mail.getText().toString(),
                                password.getText().toString()),
                        address.getText().toString(),
                        dunsNumber.getText().toString()));
                ((Activity)getmContext()).finish();
            } catch (UserAlreadySignUpException e) {
                showMessage(getString(R.string.business_with_this_email_already_exist));
            } catch (ConnectionException e) {
                showMessage(getString(R.string.connection_error));
            }
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
     * Control if all the data insert by the user in the registration form are valid.
     *
     * @return true if the data insert are acceptable, false otherwise.
     */
    private boolean checkConstraintOnData() {
        if (companyName.getText().toString().isEmpty() ||
                mail.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty() ||
                dunsNumber.getText().toString().isEmpty()) {
            showMessage(getString(R.string.no_field_must_be_empty));
            return false;
        }
        if (!mail.getText().toString().matches(Constant.E_MAIL_PATTERN)) {
            mail.setError(getString(R.string.email_is_not_valid));
            return false;
        }
        if (!terms.isChecked()) {
            showMessage(getString(R.string.terms_and_condition_error));
            return false;
        }
        return true;
    }
}
