package com.trackme.trackmeapplication.request.individualRequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Body of the message
 *
 * @author Mattia Tibaldi
 */
public class IndividualRequestBusinessBodyActivity extends AppCompatActivity {

    @BindView(R.id.textViewThirdPartyName)
    protected TextView thirdPartyName;
    @BindView(R.id.textViewRequestBodyPeriod)
    protected TextView period;
    @BindView(R.id.textViewRequestBodyMotive)
    protected TextView motive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_request_business_body);

        ButterKnife.bind(this);

        IndividualRequestWrapper individualRequestWrapper = (IndividualRequestWrapper) getIntent().getSerializableExtra(Constant.SD_INDIVIDUAL_REQUEST_KEY);

        thirdPartyName.setText(individualRequestWrapper.getUserSsn());
        String s = individualRequestWrapper.getStartDate() + " to " + individualRequestWrapper.getEndDate();
        period.setText(s);
        motive.setText(individualRequestWrapper.getMotivation());
    }
}
