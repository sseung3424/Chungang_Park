package com.chungangpark.parknavigator;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
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
    private ConnectThread connectThread;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_arduino);

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        TextView btReadings = findViewById(R.id.btReadings);
        TextView btDevices = findViewById(R.id.btDevices);

        // 뒤로가기 버튼 클릭 리스너 설정
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 명시적으로 이동
                Intent intent = new Intent(BluetoothController.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 기존 액티비티 스택 제거
                startActivity(intent);
                finish();  // 현재 액티비티 종료
            }
        });

        // 권한 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
        }

        // 핸들러로 오류 메시지를 처리
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ERROR_READ) {
                    String arduinoMsg = msg.obj.toString();
                    btReadings.setText(arduinoMsg);
                }
            }
        };

        // 블루투스 장치 검색
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
                    }
                }
                btDevices.setText(btDevicesString.toString());

                // 블루투스 장치와 연결
                if (arduinoBTModule != null) {
                    connectThread = new ConnectThread(arduinoBTModule, arduinoUUID, handler);
                    connectThread.start();  // 스레드를 실행하는 올바른 방식

                    // 자동으로 Arduino에 명령을 보냄
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (connectThread.getMmSocket().isConnected()) {
                            sendAutoCommands();
                        }
                    }, 1000); // 연결 후 1초 후에 명령을 보냄
                }
            }
        }
    }

    // 조건이 만족될 때 자동으로 명령을 보내는 함수
    private void sendAutoCommands() {
        // 특정 조건이 충족되었을 때 자동으로 '1' 신호를 보냄
        if (connectThread != null && connectThread.getMmSocket().isConnected()) {
            connectThread.sendCommand("1");
        }

        // 예를 들어, 5초 후에 자동으로 '5' 명령을 보냄
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (connectThread != null && connectThread.getMmSocket().isConnected()) {
                connectThread.sendCommand("5");
            }
        }, 5000);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectThread != null) {
            connectThread.cancel();  // 연결 해제
        }
    }
}
