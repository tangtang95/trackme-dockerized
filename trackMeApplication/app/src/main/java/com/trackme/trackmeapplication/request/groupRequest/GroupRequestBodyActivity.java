package com.trackme.trackmeapplication.request.groupRequest;

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
public class GroupRequestBodyActivity extends AppCompatActivity {

    @BindView(R.id.textViewCreationDate)
    protected TextView creationDate;
    @BindView(R.id.textViewAggregator)
    protected TextView aggregator;
    @BindView(R.id.textViewRequestType)
    protected TextView requestType;
    @BindView(R.id.textViewStatus)
    protected TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_request_body);
        ButterKnife.bind(this);

        GroupRequestWrapper groupRequestWrapper = (GroupRequestWrapper) getIntent().getSerializableExtra(Constant.SD_GROUP_REQUEST_KEY);

        creationDate.setText(groupRequestWrapper.getCreationTimestamp());
        aggregator.setText(groupRequestWrapper.getAggregatorOperator());
        requestType.setText(groupRequestWrapper.getRequestType());
        status.setText(groupRequestWrapper.getStatus().toString());
    }
}
