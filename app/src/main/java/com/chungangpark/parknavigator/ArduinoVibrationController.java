package com.chungangpark.parknavigator;

import java.io.IOException;
import java.io.OutputStream;

public class ArduinoVibrationController {
    // 필드 추가 (클래스 맨 위에 추가)
    private OutputStream outputStream;
    // 아두이노로 진동 신호 전송 함수 추가
    public void sendVibrationSignal() {
        try {
            if (outputStream != null) {
                outputStream.write("V".getBytes()); // 진동 신호 전송
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 진동 신호 전송 함수 추가
        try {
            if (outputStream != null) {
                outputStream.write("V".getBytes()); // 진동 신호 전송
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 진동 신호 반복 전송 함수 추가
    public void sendRepeatedVibrationSignal(int times) {
        new Thread(() -> {
            for (int i = 0; i < times; i++) {
                sendVibrationSignal(); // 진동 신호 전송
                try {
                    Thread.sleep(500); // 0.5초 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
