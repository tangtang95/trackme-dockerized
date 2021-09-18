package com.trackme.bluetoothclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothClientConnect mBluetoothClientConnect;

    @BindView(R.id.send_button)
    public Button sendButton;

    @BindView(R.id.heartbeat)
    public EditText heartBeatText;

    @BindView(R.id.pressure_min)
    public EditText pressureMinText;

    @BindView(R.id.pressure_max)
    public EditText pressureMaxText;

    @BindView(R.id.blood_oxygen_level)
    public EditText bloodOxygenLevelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);

        sendButton.setEnabled(false);

        String deviceNameText = getIntent().getStringExtra(getString(R.string.device_name_extra));
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled()) {
            Log.d(getString(R.string.debug_tag), "Bluetooth is enabled");
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                Log.d(getString(R.string.debug_tag), "Has paired device");

                BluetoothDevice mDevice = null;

                for (BluetoothDevice device : pairedDevices) {
                    Log.d(getString(R.string.debug_tag), device.getName());
                    if (deviceNameText.equals(device.getName()))
                        mDevice = device;
                }
                if (mDevice != null) {
                    Log.d(getString(R.string.debug_tag), "Start bluetooth client");
                    mBluetoothClientConnect = new BluetoothClientConnect(mBluetoothAdapter, mDevice, sendButton);
                    mBluetoothClientConnect.run();
                }
            }
        } else{
            Toast.makeText(getApplicationContext(), R.string.bluetooth_disabled, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.send_button)
    public void OnSendButtonClick(){
        Calendar calendar = new GregorianCalendar();
        HealthData healthData = new HealthData();
        healthData.setTimestamp(new Timestamp(calendar.getTime().getTime()));
        healthData.setHeartbeat(Integer.valueOf(heartBeatText.getText().toString()));
        healthData.setPressureMin(Integer.valueOf(pressureMinText.getText().toString()));
        healthData.setPressureMax(Integer.valueOf(pressureMaxText.getText().toString()));
        healthData.setBloodOxygenLevel(Integer.valueOf(bloodOxygenLevelText.getText().toString()));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String healthDataJson = objectMapper.writeValueAsString(healthData);
            mBluetoothClientConnect.sendData(healthDataJson);
            Log.d(getString(R.string.debug_tag), healthDataJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
