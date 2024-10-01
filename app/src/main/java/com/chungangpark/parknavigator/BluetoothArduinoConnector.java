package com.chungangpark.parknavigator;

// 블루투스 관련 라이브러리 임포트 확인
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


public class BluetoothArduinoConnector {
    // 필드 추가 (클래스 맨 위에 추가)
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;

    // 블루투스 설정 함수 추가
    /*private void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show(); // this 대신 getApplicationContext() 사용
            return;
        }

        // 블루투스 장치 연결 (아두이노의 블루투스 모듈 MAC 주소와 UUID 설정)
        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice("00:00:00:00:00:00"); // 아두이노 블루투스 모듈의 MAC 주소로 변경
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 일반적인 시리얼 통신 UUID
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Bluetooth connection failed", Toast.LENGTH_SHORT).show();
        }
    }*/

}
