package com.trackme.trackmeapplication.request.individualRequest;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkIInterface;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkImp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Request body class shows to the user the body of the request message and it allows user to accept
 * or refuse the request.
 *
 * @author Mattia Tibaldi
 */
public class RequestBodyActivity extends AppCompatActivity {

    @BindView(R.id.textViewThirdPartyName)
    protected TextView thirdPartyName;
    @BindView(R.id.textViewRequestBodyPeriod)
    protected TextView period;
    @BindView(R.id.textViewRequestBodyMotive)
    protected TextView motive;

    private String token;

    IndividualRequestNetworkIInterface individualrequestNetwork = IndividualRequestNetworkImp.getInstance();
    IndividualRequestWrapper individualRequestWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_body);

        individualRequestWrapper = (IndividualRequestWrapper) getIntent().getSerializableExtra(Constant.SD_INDIVIDUAL_REQUEST_KEY);
        SharedPreferences sp = getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
        token = sp.getString(Constant.SD_USER_TOKEN_KEY, null);

        ButterKnife.bind(this);

        thirdPartyName.setText(individualRequestWrapper.getThirdPartyName());
        String s = individualRequestWrapper.getStartDate() + " to " + individualRequestWrapper.getEndDate();
        period.setText(s);
        motive.setText(individualRequestWrapper.getMotivation());
    }

    /**
     * Handle the accept button click event.
     */
    @OnClick(R.id.requestBodyAccept)
    public void onRequestAcceptClick() {
        try {
            individualrequestNetwork.acceptIndividualRequest(token, individualRequestWrapper.extractResponseLink());
        } catch (ConnectionException e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    /**
     * Handle the refuse button click event.
     */
    @OnClick(R.id.requestBodyRefuse)
    public void onRequestRefuseClick() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        String blockUrl = individualrequestNetwork.refuseIndividualRequest(token, individualRequestWrapper.extractResponseLink());
                        individualrequestNetwork.blockThirdPartyCustomer(token, blockUrl);
                    } catch (ConnectionException e) {
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    try {
                        individualrequestNetwork.refuseIndividualRequest(token, individualRequestWrapper.extractResponseLink());
                    } catch (ConnectionException e) {
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.block_third_party_message)).
                setPositiveButton(android.R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.no, dialogClickListener).show();
    }
}
