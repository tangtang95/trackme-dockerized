package com.trackme.bluetoothclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class BluetoothClientConnect extends Thread{

    private static final UUID MY_UUID = UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");

    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private final BluetoothAdapter mAdapter;
    private final Button mButton;
    private ObjectOutputStream mObjectOutputStream;

    public BluetoothClientConnect(BluetoothAdapter adapter, BluetoothDevice device, Button button) {
        mButton = button;
        mAdapter = adapter;
        BluetoothSocket tmp = null;
        mDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(this.getName(), "Socket's create() method failed", e);
        }
        mSocket = tmp;
    }

    public void run() {
        mAdapter.cancelDiscovery();

        try {
            mSocket.connect();
        } catch (IOException connectException) {
            try {
                mSocket.close();
            } catch (IOException closeException) {
                Log.e(this.getName(), "Could not close the client socket", closeException);
            }
            return;
        }
        try {
            mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        } catch (IOException e) {
            Log.e(this.getName(), "Could not close the client socket", e);
        }
        enableSendButton();
    }

    private void enableSendButton() {
        mButton.setEnabled(true);
    }

    public void sendData(String healthDataJson){
        try {
            mObjectOutputStream.writeObject(healthDataJson);
            mObjectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the client socket and causes the thread to finish.
     */
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(this.getName(), "Could not close the client socket", e);
        }
    }
}
