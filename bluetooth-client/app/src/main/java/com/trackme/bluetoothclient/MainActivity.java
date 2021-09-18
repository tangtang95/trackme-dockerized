package com.trackme.bluetoothclient;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    public EditText deviceNameText;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.d(getString(R.string.debug_tag), "onCreate Main Activity");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @OnClick(R.id.connect_button)
    public void onClickConnectButton(){
        Log.d(getString(R.string.debug_tag), "start new bluetooth activity");
        if (mBluetoothAdapter != null) {
            Log.d(getString(R.string.debug_tag), "start new bluetooth activity");
            Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
            intent.putExtra(getString(R.string.device_name_extra), deviceNameText.getText().toString());
            startActivity(intent);
        }else{
            Log.d(getString(R.string.debug_tag), "Cannot start bluetooth activity");
            Toast.makeText(getApplicationContext(), R.string.bluetooth_error, Toast.LENGTH_LONG).show();
        }
    }
}
