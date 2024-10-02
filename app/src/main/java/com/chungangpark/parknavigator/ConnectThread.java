package com.chungangpark.parknavigator;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

// 클래스는 아두이노 블루투스 모듈에 소켓을 열고 연결합니다.
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final OutputStream mmOutStream;
    private static final String TAG = "ConnectThread";
    public static Handler handler;
    private static final int ERROR_READ = 0;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, UUID MY_UUID, Handler handler) {
        BluetoothSocket tmp = null;
        OutputStream tmpOut = null;
        this.handler = handler;

        try {
            // Bluetooth 소켓 생성
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            tmpOut = tmp.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "소켓 생성 실패", e);
        }
        mmSocket = tmp;
        mmOutStream = tmpOut;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        try {
            mmSocket.connect(); // 소켓 연결
        } catch (IOException connectException) {
            handler.obtainMessage(ERROR_READ, "블루투스 연결 실패").sendToTarget();
            Log.e(TAG, "연결 실패: " + connectException);
            try {
                mmSocket.close(); // 실패 시 소켓 닫기
            } catch (IOException closeException) {
                Log.e(TAG, "소켓 닫기 실패", closeException);
            }
            return;
        }
    }

    // 연결된 소켓을 닫음
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "소켓 닫기 실패", e);
        }
    }

    // 진동 모터 제어 명령을 보내는 함수 추가
    public void sendCommand(String command) {
        try {
            mmOutStream.write(command.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "명령 전송 실패", e);
        }
    }

    public BluetoothSocket getMmSocket() {
        return mmSocket;
    }
}
