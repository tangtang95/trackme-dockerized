package com.trackme.trackmeapplication.request.groupRequest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.exception.ThirdPartyBlockedException;
import com.trackme.trackmeapplication.request.groupRequest.network.GroupRequestNetworkImp;
import com.trackme.trackmeapplication.request.groupRequest.network.GroupRequestNetworkInterface;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Group request Form activity is a class that shows to the user a form for compiling a group request
 * message and send it to the server.
 *
 * @author Mattia Tibaldi
 */
public class GroupRequestFormActivity extends AppCompatActivity {

    /*Bind the object on the layout*/
    @BindView(R.id.spinnerAggregator)
    protected Spinner spinnerAggregator;
    @BindView(R.id.spinnerType)
    protected Spinner spinnerType;
    @BindView(R.id.spinnerColumn)
    protected Spinner spinnerColumn;
    @BindView(R.id.spinnerOperator)
    protected Spinner spinnerOperator;
    @BindView(R.id.imageViewSend)
    protected ImageView send;
    @BindView(R.id.imageViewPlus)
    protected ImageView plus;
    @BindView(R.id.imageViewMin)
    protected ImageView remove;
    @BindView(R.id.editTextValue)
    protected EditText value;
    @BindView(R.id.textViewFilterData)
    protected TextView filter;

    private GroupRequestNetworkInterface groupRequestNetwork = GroupRequestNetworkImp.getInstance();
    private SharedPreferences sp;

    /**
     * Load the activity layout and get from the server all the value useful for compiling the request.
     *
     * @param savedInstanceState the last save instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_request_form);

        ButterKnife.bind(this);

        sp = getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);

        filter.setMovementMethod(new ScrollingMovementMethod());

        List<String> aggregators = groupRequestNetwork.getAggregators();
        List<String> types = groupRequestNetwork.getRequestTypes();
        List<String> columns = groupRequestNetwork.getDbColumns();
        List<String> operators = groupRequestNetwork.getOperators();

        ArrayAdapter<String> aggregatorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aggregators);
        aggregatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAggregator.setAdapter(aggregatorAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<String> columnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, columns);
        columnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColumn.setAdapter(columnAdapter);

        ArrayAdapter<String> operatorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operators);
        operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperator.setAdapter(operatorAdapter);

    }

    /**
     * Add a filter to the filter textView.
     */
    @OnClick(R.id.imageViewPlus)
    public void onPlusButtonClick() {
        String column = spinnerColumn.getSelectedItem().toString();
        String operator = spinnerOperator.getSelectedItem().toString();
        String n = value.getText().toString();
        if (n.isEmpty())
            value.setError(getString(R.string.no_field_must_be_empty));
        else {
            if (filter.getText().toString().isEmpty()) {
                String f = column + " " + operator + " " + n;
                filter.setText(f);
            } else {
                String f = filter.getText().toString() + "+" + column + " " + operator + " " + n;
                filter.setText(f);
            }
        }

    }

    /**
     * Remove a filter from the textView.
     */
    @OnClick(R.id.imageViewMin)
    public void onRemoveButtonClick() {
        String f = filter.getText().toString();
        StringBuilder newF = new StringBuilder();
        if (!f.isEmpty()) {
            String[] filterItem = f.split("\\+");
            for (int i = 0; i < filterItem.length - 1; i++) {
                newF.append(filterItem[i]);
                if (i != filterItem.length - 2)
                    newF.append("+");
            }
        }
        filter.setText(newF.toString());
    }

    /**
     * Send the group request to the server.
     */
    @OnClick(R.id.imageViewSend)
    public void onSendButtonClick() {
        try {

            GroupRequestBuilder groupRequestBuilder = new GroupRequestBuilder();

            if (!filter.getText().toString().isEmpty())
                groupRequestBuilder.addNewFilter(parseFilter(filter.getText().toString()));

            GroupRequest groupRequest = new GroupRequest(
                    spinnerAggregator.getSelectedItem().toString(),
                    spinnerType.getSelectedItem().toString()
            );
            groupRequestBuilder.setGroupRequest(groupRequest);
            groupRequestNetwork.send(sp.getString(Constant.SD_BUSINESS_TOKEN_KEY, null), groupRequestBuilder);

            finish();
        } catch (ConnectionException e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        } catch (RequestNotWellFormedException e) {
            Toast.makeText(this, getString(R.string.request_not_well_formed), Toast.LENGTH_SHORT).show();
        } catch (ThirdPartyBlockedException e) {
            Toast.makeText(this, getString(R.string.third_party_is_block), Toast.LENGTH_SHORT).show();
        } catch (UserNotFoundException e) {
            Toast.makeText(this, getString(R.string.impossible_to_find_user), Toast.LENGTH_SHORT).show();
        }


    }

    private String[] parseFilter(String filter) {
        return filter.split("\\+");
    }

}
