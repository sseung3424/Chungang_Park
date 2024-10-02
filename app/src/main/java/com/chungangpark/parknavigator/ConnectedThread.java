package com.chungangpark.parknavigator;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

// 연결된 소켓을 통해 아두이노에 데이터를 보내는 역할만 수행하는 클래스
public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private final BluetoothSocket mmSocket;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        OutputStream tmpOut = null;

        // 소켓의 출력 스트림을 가져옴
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "출력 스트림 생성 실패", e);
        }
        mmOutStream = tmpOut;
    }

    // 명령을 아두이노로 전송하는 메서드
    public void sendCommand(String command) {
        try {
            mmOutStream.write(command.getBytes()); // 명령을 보냄
        } catch (IOException e) {
            Log.e(TAG, "명령 전송 실패", e);
        }
    }

    // 소켓을 닫는 메서드
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "소켓 닫기 실패", e);
        }
    }
}
