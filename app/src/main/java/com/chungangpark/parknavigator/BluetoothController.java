package com.chungangpark.parknavigator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BluetoothController extends AppCompatActivity {
    private static final String TAG = "BluetoothController";
    private static final int REQUEST_ENABLE_BT = 1;

    public static Handler handler;
    private static final int ERROR_READ = 0;
    private BluetoothDevice arduinoBTModule = null;
    private UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_arduino);

        // Bluetooth 관련 객체 생성
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        // UI 컴포넌트 설정
        TextView btReadings = findViewById(R.id.btReadings);
        TextView btDevices = findViewById(R.id.btDevices);
        Button connectToDevice = findViewById(R.id.connectToDevice);
        Button searchDevices = findViewById(R.id.seachDevices);
        Button clearValues = findViewById(R.id.refresh);

        // 권한 체크 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
        }

        // 연결 실패 시 메시지 핸들링
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ERROR_READ) {
                    String arduinoMsg = msg.obj.toString();
                    btReadings.setText(arduinoMsg);
                }
            }
        };

        // 연결된 장치 목록 초기화
        clearValues.setOnClickListener(v -> {
            btDevices.setText("");
            btReadings.setText("");
        });

        // Observable 생성 - Bluetooth 장치 연결
        final Observable<String> connectToBTObservable = Observable.create(emitter -> {
            ConnectThread connectThread = new ConnectThread(arduinoBTModule, arduinoUUID, handler);
            connectThread.run();

            if (connectThread.getMmSocket().isConnected()) {
                ConnectedThread connectedThread = new ConnectedThread(connectThread.getMmSocket());
                connectedThread.run();

                if (connectedThread.getValueRead() != null) {
                    emitter.onNext(connectedThread.getValueRead());
                }

                connectedThread.cancel();
            }
            connectThread.cancel();
            emitter.onComplete();
        });

        // 장치 연결 버튼 리스너 설정
        connectToDevice.setOnClickListener(v -> {
            btReadings.setText("");
            if (arduinoBTModule != null) {
                connectToBTObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(valueRead -> btReadings.setText(valueRead));
            }
        });

        // 블루투스 장치 검색 버튼 리스너 설정
        searchDevices.setOnClickListener(v -> {
            if (bluetoothAdapter == null) {
                Log.d(TAG, "Device doesn't support Bluetooth");
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                StringBuilder btDevicesString = new StringBuilder();
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                if (!pairedDevices.isEmpty()) {
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();
                        btDevicesString.append(deviceName).append(" || ").append(deviceHardwareAddress).append("\n");

                        if (deviceName.equals("HC-05")) {
                            arduinoUUID = device.getUuids()[0].getUuid();
                            arduinoBTModule = device;
                            connectToDevice.setEnabled(true);
                        }
                    }
                    btDevices.setText(btDevicesString.toString());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Bluetooth permission granted");
            } else {
                Log.d(TAG, "Bluetooth permission denied");
            }
        }
    }
}
