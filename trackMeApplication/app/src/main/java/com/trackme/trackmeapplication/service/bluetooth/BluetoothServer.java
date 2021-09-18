package com.trackme.trackmeapplication.service.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.trackme.trackmeapplication.service.util.Constants;

import java.io.IOException;
import java.util.UUID;

public class BluetoothServer extends Thread {

    private static final UUID SERVER_UUID = UUID.fromString(Constants.BLUETOOTH_STRING_UUID);

    private final BluetoothServerSocket mServerSocket;
    private final Handler mHandler;

    /**
     * Constructor.
     * Create a new bluetooth server by listening using RFcomm based on a UUID
     *
     * @param bluetoothAdapter the adapter of the bluetooth
     * @param handler the handler of messages when a message is received
     */
    public BluetoothServer(BluetoothAdapter bluetoothAdapter, Handler handler) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(Constants.BLUETOOTH_COMM_NAME, SERVER_UUID);
        } catch (IOException e) {
            Log.e(Constants.BLUETOOTH_LOG_TAG, e.getMessage());
        }
        mServerSocket = tmp;
        mHandler = handler;
    }

    /**
     * Thread.
     * Keep listening until exception occurs or a socket is returned. When A connection is accepted,
     * performs work associated with the connection in a separate thread.
     */
    @Override
    public void run() {
        BluetoothSocket socket;
        SocketHandler socketHandler = null;
        while (true) {
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                Log.e(Constants.BLUETOOTH_LOG_TAG,e.getMessage());
                break;
            }
            if(socketHandler == null || !socketHandler.isAlive()) {
                if (socket != null) {
                    socketHandler = new SocketHandler(socket, mHandler);
                    socketHandler.start();
                }
            }
        }
        this.cancel();
    }

    /**
     * Closes the connect socket and causes the thread to finish.
     */
    public void cancel() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            Log.e(Constants.BLUETOOTH_LOG_TAG, e.getMessage());
        }
    }
}
